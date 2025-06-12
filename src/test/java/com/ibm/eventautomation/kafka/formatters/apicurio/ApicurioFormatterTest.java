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
package com.ibm.eventautomation.kafka.formatters.apicurio;

import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;
import static io.apicurio.registry.serde.SerdeConfig.REGISTRY_URL;
import static io.apicurio.registry.serde.SerdeConfig.AUTO_REGISTER_ARTIFACT;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import com.ibm.eventautomation.kafka.formatters.TestCase;
import com.ibm.eventautomation.kafka.formatters.eem.EemServer;

import io.apicurio.registry.serde.avro.AvroKafkaSerializer;

/**
 * Abstract base class for testing the Apicurio formatter using different
 *  Apicurio serdes options.
 *
 * The intent isn't to exhaustively test config options for
 *  AvroKafkaSerializer / AvroKafkaDeserializer, but rather to do enough
 *  to try a few different options to validate that the config is being
 *  correctly passed by the formatter.
 */
public abstract class ApicurioFormatterTest<T> extends TestCase {

    /**
     * Use the Apicurio AvroKafkaSerializer to produce the test record to the Kafka topic.
     */
    @Override
    public void produceTestEvent(String topic, Properties producerProperties, ApicurioContainer schemaRegistry) throws IOException {
        producerProperties.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getCanonicalName());
        producerProperties.put(VALUE_SERIALIZER_CLASS_CONFIG, AvroKafkaSerializer.class.getCanonicalName());
        producerProperties.put(REGISTRY_URL, schemaRegistry.getRegistryAddress());
        producerProperties.put(AUTO_REGISTER_ARTIFACT, true);
        setApicurioProducerProperties(producerProperties);

        try (Producer<String, T> producer = new KafkaProducer<>(producerProperties)) {
            producer.send(new ProducerRecord<>(topic, "key", createRecord()));
            producer.flush();
        }
    }

    /**
     * Individual test cases can set different Apicurio AvroKafkaSerializer serdes options.
     */
    public abstract void setApicurioProducerProperties(Properties props);

    /**
     * Returns a Java object to be serialialized for producing to Kafka.
     */
    public abstract T createRecord();

    /**
     * Configures the Apicurio formatter.
     */
    @Override
    public void addFormatterConfig(List<String> consoleConsumerOptions, ApicurioContainer schemaRegistry, EemServer eem) throws IOException {
        Map<String, String> formatterProperties = new HashMap<>();
        formatterProperties.put(REGISTRY_URL, schemaRegistry.getRegistryAddress());
        formatterProperties.put("line.separator", getSeparator().replaceAll("\n", "\\\\n"));

        consoleConsumerOptions.add("--formatter");
        consoleConsumerOptions.add("com.ibm.eventautomation.kafka.formatters.ApicurioFormatter");
        consoleConsumerOptions.add("--formatter-config");
        consoleConsumerOptions.add(createApicurioFormatterPropertiesFile(formatterProperties));
    }

    /**
     * Individual test cases can set different Apicurio AvroKafkaDeserializer serdes options
     *  to match the config in setApicurioProducerProperties().
     */
    public abstract String createApicurioFormatterPropertiesFile(Map<String, String> props) throws IOException;
}
