/**
 * Copyright 2025 IBM Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ibm.eventautomation.kafka.formatters;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;

import org.apache.avro.AvroRuntimeException;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.MessageFormatter;
import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.common.errors.SerializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class AbstractAvroFormatter implements MessageFormatter {

    private final Logger log = LoggerFactory.getLogger(AbstractAvroFormatter.class);

    private static final String CONFIG_ID_BYTES      = "id.num.bytes";


    /**
     * Number of bytes in the record value that are used to store the schema id.
     *  These will be skipped by the formatter which is going to be provided
     *  with the schema to use, so the id is not used.
     */
    private int idBytes = 0;

    private byte[] lineSeparator = null;

    /**
     * Configures the MessageFormatter
     *
     * @param configs Map to configure the formatter
     */
    @Override
    public void configure(Map<String, ?> configs) {
        if (log.isDebugEnabled()) {
            for (String key : configs.keySet()) {
                log.debug("{} = {}",
                          key,
                          key.endsWith("password") ? "******" : configs.get(key).toString());
            }
        }

        if (configs.containsKey(CONFIG_ID_BYTES)) {
            try {
                idBytes = Integer.parseInt(configs.get(CONFIG_ID_BYTES).toString());
            }
            catch (NumberFormatException nfe) {
                throw new ConfigException(CONFIG_ID_BYTES + " should contain " +
                    "the number of bytes at the start of record values that are " +
                    "used to store the schema id");
            }
        }

        if (configs.containsKey("line.separator")) {
            lineSeparator = configs.get("line.separator").toString().getBytes();
        }
    }

    /**
     * Parses and formats a record for display
     *
     * @param record the record to format
     * @param destination the print stream used to output the record
     */
    @Override
    public void writeTo(ConsumerRecord<byte[], byte[]> record, PrintStream destination) {
        try {
            Object deserializedRecord = deserialize(record);
            destination.print(
                deserializedRecord == null ?
                    "null" :
                    deserializedRecord.toString());

            if (lineSeparator != null) {
                destination.write(lineSeparator);
            }
        }
        catch (IOException ioexc) {
            log.error("Failed to write to destination", ioexc);
        }
    }


    // ----------------------------------------------------------------------

    protected abstract Object deserialize(ConsumerRecord<byte[], byte[]> record) throws IOException;

    protected Object avroDeserialize(ConsumerRecord<byte[], byte[]> record, BinaryDecoder decoder, DatumReader<GenericRecord> datumReader) throws IOException {
        try {
            ByteArrayInputStream input = createByteArrayInputStream(record.value());
            decoder = DecoderFactory.get().directBinaryDecoder(input, decoder);
            return datumReader.read(null, decoder);
        }
        catch (AvroRuntimeException | SerializationException | ArrayIndexOutOfBoundsException | IOException deserException) {
            log.error("Failed to deserialize record", deserException);
            return String.format("Unable to deserialize event (%s)", deserException.getMessage());
        }
    }



    private ByteArrayInputStream createByteArrayInputStream(byte[] data) {
        // if the ID is stored in the data, it will be prefixed with an initial
        //  zero "magic" byte, which needs to be skipped as well as the ID itself
        final int idLength = (idBytes == 0) ? 0 : idBytes + 1;

        // return an input stream containing the data, minus any schema ID
        //  that may be stored at the start of the data
        return new ByteArrayInputStream(data, idLength, data.length - idLength);
    }
}
