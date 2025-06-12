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
package com.ibm.eventautomation.kafka.formatters.string;

import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;

import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import com.ibm.eventautomation.kafka.formatters.TestCase;
import com.ibm.eventautomation.kafka.formatters.apicurio.ApicurioContainer;
import com.ibm.eventautomation.kafka.formatters.eem.EemServer;



public class DefaultFormatterTest extends TestCase {

    private static final String TEST_MESSAGE = "This is a message.";
    private static final String DEFAULT_SEPARATOR = "\n";

    @Override
    public String getSeparator() {
        return DEFAULT_SEPARATOR;
    }

    @Override
    public String getExpectedFormattedOutput() {
        return TEST_MESSAGE;
    }

    @Override
    public void produceTestEvent(String topic, Properties producerProperties, ApicurioContainer schemaRegistry) {
        producerProperties.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getCanonicalName());
        producerProperties.put(VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getCanonicalName());

        try (Producer<String, String> producer = new KafkaProducer<>(producerProperties)) {
            producer.send(new ProducerRecord<>(topic, "key", TEST_MESSAGE));
            producer.flush();
        }
    }

    @Override
    public void addFormatterConfig(List<String> consoleConsumerOptions, ApicurioContainer schemaRegistry, EemServer eem) { }
}
