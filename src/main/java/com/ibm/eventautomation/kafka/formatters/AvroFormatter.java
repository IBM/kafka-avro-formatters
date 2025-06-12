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
import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.config.ConfigException;


/**
 * Formatter for Avro-encoded Kafka events where the schema can be provided
 *  as a file.
 *
 * The location of the file should be provided using the "schema.file" config option.
 */
public class AvroFormatter extends AbstractAvroFormatter {

    private DatumReader<GenericRecord> datumReader = null;
    private BinaryDecoder decoder = null;


    @Override
    public void configure(Map<String, ?> configs) {
        super.configure(configs);

        if (!configs.containsKey("schema.file")) {
            throw new ConfigException("Missing required property 'schema' with the location of an Avro schema");
        }

        String location = (String) configs.get("schema.file");
        File schemaFile = new File(location);
        if (!schemaFile.exists() || !schemaFile.isFile()) {
            throw new ConfigException("'schema' property should point at the location of an Avro schema file");
        }

        try {
            Schema schema = new Schema.Parser().parse(schemaFile);
            datumReader = new GenericDatumReader<>(schema);

            ByteArrayInputStream initInput = new ByteArrayInputStream(new byte[0]);
            decoder = DecoderFactory.get().directBinaryDecoder(initInput, null);
        }
        catch (IOException e) {
            throw new ConfigException(e.getMessage());
        }
    }

    @Override
    protected Object deserialize(ConsumerRecord<byte[], byte[]> record) throws IOException {
        return avroDeserialize(record, decoder, datumReader);
    }
}
