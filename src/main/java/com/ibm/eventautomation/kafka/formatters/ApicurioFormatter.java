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

import java.io.IOException;
import java.util.Map;

import org.apache.avro.generic.GenericData.Record;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.errors.SerializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.apicurio.registry.serde.avro.AvroKafkaDeserializer;


/**
 * Formatter for Avro-encoded Kafka events where the schema can be retrieved
 *  from a schema registry.
 *
 * The provided config is used to create and configure an instance of
 *  io.apicurio.registry.serde.avro.AvroKafkaDeserializer, so any config
 *  options supported by AvroKafkaDeserializer can be provided.
 *
 * ref: https://www.apicur.io/registry/docs/apicurio-registry/2.6.x/getting-started/assembly-configuring-kafka-client-serdes.html
 */
public class ApicurioFormatter extends AbstractAvroFormatter {

    private final Logger log = LoggerFactory.getLogger(ApicurioFormatter.class);


    private AvroKafkaDeserializer<Record> apicurioDeserializer = null;


    /**
     * Configures the MessageFormatter
     *
     * @param configs Map to configure the formatter
     */
    @Override
    public void configure(Map<String, ?> configs) {
        super.configure(configs);

        apicurioDeserializer = new AvroKafkaDeserializer<>();
        apicurioDeserializer.configure(configs, false);
    }


    @Override
    protected Object deserialize(ConsumerRecord<byte[], byte[]> record) throws IOException {
        try {
            return apicurioDeserializer.deserialize(record.topic(), record.headers(), record.value());
        }
        catch (SerializationException deserException) {
            log.error("Failed to deserialize record", deserException);
            return String.format("Unable to deserialize event (%s)", deserException.getMessage());
        }
    }
}
