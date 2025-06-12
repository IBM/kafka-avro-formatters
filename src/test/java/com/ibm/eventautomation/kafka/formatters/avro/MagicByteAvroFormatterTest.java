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
package com.ibm.eventautomation.kafka.formatters.avro;

import static io.apicurio.registry.serde.SerdeConfig.AUTO_REGISTER_ARTIFACT;
import static io.apicurio.registry.serde.SerdeConfig.ENABLE_HEADERS;
import static io.apicurio.registry.serde.SerdeConfig.ID_HANDLER;
import static io.apicurio.registry.serde.SerdeConfig.REGISTRY_URL;
import static io.apicurio.registry.serde.SerdeConfig.USE_ID;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.GenericRecordBuilder;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import com.ibm.eventautomation.kafka.formatters.apicurio.ApicurioContainer;
import com.ibm.eventautomation.kafka.formatters.eem.EemServer;
import com.ibm.eventautomation.kafka.schemas.SampleRecord;

import io.apicurio.registry.serde.avro.AvroKafkaSerializer;
import io.apicurio.registry.serde.config.IdOption;

public class MagicByteAvroFormatterTest extends AvroFormatterTest {

    private static final String SEPARATOR = "\n";

    /** io.apicurio.registry.serde.IdHandler to use when producing the test message */
    private final String idHandler;

    /** Number of bytes to skip after the "magic" byte eye-catcher */
    private final int numIdBytes;


    public MagicByteAvroFormatterTest(String idHandler, int idBytes) throws IOException {
        super();
        this.idHandler = idHandler;
        this.numIdBytes = idBytes;
    }


    /**
     * Use the Apicurio AvroKafkaSerializer to produce the test record to the Kafka topic
     *  using a Confluent-style ("magic byte") ID embedded in the payload.
     */
    @Override
    public void produceTestEvent(String topic, Properties producerProperties, ApicurioContainer schemaRegistry) throws IOException {
        producerProperties.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getCanonicalName());
        producerProperties.put(VALUE_SERIALIZER_CLASS_CONFIG, AvroKafkaSerializer.class.getCanonicalName());
        producerProperties.put(REGISTRY_URL, schemaRegistry.getRegistryAddress());
        producerProperties.put(AUTO_REGISTER_ARTIFACT, true);
        producerProperties.put(USE_ID, IdOption.contentId.name());
        producerProperties.put(ENABLE_HEADERS, false);
        producerProperties.put(ID_HANDLER, idHandler);

        try (Producer<String, SampleRecord> producer = new KafkaProducer<>(producerProperties)) {
            producer.send(new ProducerRecord<>(topic, "key", createRecord()));
            producer.flush();
        }
    }


    @Override
    public void addFormatterConfig(List<String> consoleConsumerOptions, ApicurioContainer schemaRegistry, EemServer eem) throws IOException {
        Map<String, String> formatterProperties = new HashMap<>();
        formatterProperties.put("schema.file", new File(getClass().getClassLoader().getResource(getSchemaFile()).getFile()).getAbsolutePath());
        formatterProperties.put("id.num.bytes", Integer.toString(numIdBytes));
        formatterProperties.put("line.separator", getSeparator().replaceAll("\n", "\\\\n"));

        consoleConsumerOptions.add("--formatter");
        consoleConsumerOptions.add("com.ibm.eventautomation.kafka.formatters.AvroFormatter");
        consoleConsumerOptions.add("--formatter-config");
        consoleConsumerOptions.add(createPropertiesFile(formatterProperties));
    }


    @Override
    public GenericRecord createAvroRecord(Schema schema, GenericRecordBuilder builder) {
        return null;
    }

    private SampleRecord createRecord() {
        return new SampleRecord(
            "52d665a8-0963-4ee5-8cd3-9e7a1c7c14bc",
            "Magic Byte schema object name",
            123.45,
            1);
    }

    @Override
    public String getExpectedFormattedOutput() {
        return
            "{" +
                "\"id\": \"52d665a8-0963-4ee5-8cd3-9e7a1c7c14bc\", " +
                "\"name\": \"Magic Byte schema object name\", " +
                "\"price\": 123.45, " +
                "\"quantity\": 1" +
            "}";
    }

    @Override
    public String getSeparator() {
        return SEPARATOR;
    }

    @Override
    public String getSchemaFile() {
        return "avro/simple.avro";
    }
}
