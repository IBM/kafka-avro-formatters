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

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.tools.consumer.ConsoleConsumer;
import org.apache.kafka.tools.consumer.ConsoleConsumerOptions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.kafka.KafkaContainer;

import com.ibm.eventautomation.kafka.formatters.apicurio.ApicurioContainer;
import com.ibm.eventautomation.kafka.formatters.apicurio.HeadersApicurioFormatterTest;
import com.ibm.eventautomation.kafka.formatters.apicurio.MagicByteApicurioFormatterTest;
import com.ibm.eventautomation.kafka.formatters.apicurio.OrderApicurioFormatterTest;
import com.ibm.eventautomation.kafka.formatters.apicurio.SimpleApicurioFormatterTest;
import com.ibm.eventautomation.kafka.formatters.avro.AllTypesAvroFormatterTest;
import com.ibm.eventautomation.kafka.formatters.avro.MagicByteAvroFormatterTest;
import com.ibm.eventautomation.kafka.formatters.avro.OrderAvroFormatterTest;
import com.ibm.eventautomation.kafka.formatters.avro.PoisonEventAvroFormatterTest;
import com.ibm.eventautomation.kafka.formatters.avro.SimpleAvroFormatterTest;
import com.ibm.eventautomation.kafka.formatters.eem.EemFormatterMissingTopicTest;
import com.ibm.eventautomation.kafka.formatters.eem.EemServer;
import com.ibm.eventautomation.kafka.formatters.eem.MagicByteEemFormatterTest;
import com.ibm.eventautomation.kafka.formatters.eem.OrderEemFormatterTest;
import com.ibm.eventautomation.kafka.formatters.eem.PoisonEventEemFormatterTest;
import com.ibm.eventautomation.kafka.formatters.string.DefaultFormatterTest;

import io.apicurio.registry.serde.DefaultIdHandler;
import io.apicurio.registry.serde.Legacy4ByteIdHandler;


public class TestRunner {

    private static KafkaContainer kafka = new KafkaContainer("apache/kafka:3.9.1-rc1");
    private static ApicurioContainer schemaregistry = new ApicurioContainer();
    private static EemServer eem = new EemServer();


    static int TOPIC_SUFFIX = 0;


    @BeforeAll
    static void setup() throws IOException {
        kafka.start();
        schemaregistry.start();
        eem.start();
    }

    @AfterAll
    static void cleanup() {
        kafka.stop();
        schemaregistry.stop();
        eem.stop();
    }

    // ---------------------------------------------------------------------
    // STRING formatter
    // ---------------------------------------------------------------------

    @Test
    public void defaultFormatter() throws IOException, InterruptedException, ExecutionException {
        runTest(new DefaultFormatterTest());
    }

    // ---------------------------------------------------------------------
    // AVRO formatter
    // ---------------------------------------------------------------------

    @Test
    public void simpleAvroFormatter() throws IOException, InterruptedException, ExecutionException {
        runTest(new SimpleAvroFormatterTest());
    }

    @Test
    public void ordersAvroFormatter() throws IOException, InterruptedException, ExecutionException {
        runTest(new OrderAvroFormatterTest());
    }

    @Test
    public void allTypesAvroFormatter() throws IOException, InterruptedException, ExecutionException {
        runTest(new AllTypesAvroFormatterTest());
    }

    @Test
    public void intMagicByteAvroFormatter() throws IOException, InterruptedException, ExecutionException {
        final int bytesToSkip = 4;
        final Class<Legacy4ByteIdHandler> idHandler = Legacy4ByteIdHandler.class;
        runTest(new MagicByteAvroFormatterTest(idHandler.getCanonicalName(), bytesToSkip));
    }

    @Test
    public void longMagicByteAvroFormatter2() throws IOException, InterruptedException, ExecutionException {
        final int bytesToSkip = 8;
        final Class<DefaultIdHandler> idHandler = DefaultIdHandler.class;
        runTest(new MagicByteAvroFormatterTest(idHandler.getCanonicalName(), bytesToSkip));
    }

    @Test
    public void deserializeFailAvroFormatter() throws IOException, InterruptedException, ExecutionException {
        runTest(new PoisonEventAvroFormatterTest());
    }


    // ---------------------------------------------------------------------
    // APICURIO formatter
    // ---------------------------------------------------------------------

    @Test
    public void simpleApicurioFormatter() throws IOException, InterruptedException, ExecutionException {
        runTest(new SimpleApicurioFormatterTest());
    }

    @Test
    public void headersApicurioFormatter() throws IOException, InterruptedException, ExecutionException {
        runTest(new HeadersApicurioFormatterTest());
    }

    @Test
    public void magicByteApicurioFormatter() throws IOException, InterruptedException, ExecutionException {
        runTest(new MagicByteApicurioFormatterTest());
    }

    @Test
    public void ordersApicurioFormatter() throws IOException, InterruptedException, ExecutionException {
        runTest(new OrderApicurioFormatterTest());
    }

    // ---------------------------------------------------------------------
    // EEM formatter
    // ---------------------------------------------------------------------

    @Test
    public void ordersEEMFormatter() throws IOException, InterruptedException, ExecutionException {
        runTest(new OrderEemFormatterTest(false));
    }

    @Test
    public void ordersEEMFormatterSkipCertValidation() throws IOException, InterruptedException, ExecutionException {
        runTest(new OrderEemFormatterTest(true));
    }

    @Test
    public void intMagicByteEemFormatter() throws IOException, InterruptedException, ExecutionException {
        final int bytesToSkip = 4;
        final Class<Legacy4ByteIdHandler> idHandler = Legacy4ByteIdHandler.class;
        runTest(new MagicByteEemFormatterTest(idHandler.getCanonicalName(), bytesToSkip));
    }

    @Test
    public void longMagicByteEemFormatter() throws IOException, InterruptedException, ExecutionException {
        final int bytesToSkip = 8;
        final Class<DefaultIdHandler> idHandler = DefaultIdHandler.class;
        runTest(new MagicByteEemFormatterTest(idHandler.getCanonicalName(), bytesToSkip));
    }

    @Test
    public void deserializeFailEEMFormatter() throws IOException, InterruptedException, ExecutionException {
        runTest(new PoisonEventEemFormatterTest());
    }

    @Test
    public void topicNotInEEMCatalog() throws IOException, InterruptedException, ExecutionException {
        runConfigErrorTest(new EemFormatterMissingTopicTest(), "Schema not available in Event Endpoint Management catalog");
    }


    private static void runTest(TestCase testCase) throws IOException, InterruptedException, ExecutionException {
        // get a new topic for this test case
        final String topic = testCase.getTopicName(++TOPIC_SUFFIX);

        // produce a test Avro message to the topic
        Properties producerProps = new Properties();
        producerProps.put("bootstrap.servers", kafka.getBootstrapServers());
        testCase.produceTestEvent(topic, producerProps, schemaregistry);

        // configure kafka-console-consumer.sh to read the test message using a message formatter
        List<String> consoleConsumerArgs = new ArrayList<>();
        consoleConsumerArgs.add("--bootstrap-server");
        consoleConsumerArgs.add(kafka.getBootstrapServers());
        consoleConsumerArgs.add("--topic");
        consoleConsumerArgs.add(topic);
        consoleConsumerArgs.add("--group");
        consoleConsumerArgs.add("group" + TOPIC_SUFFIX);
        consoleConsumerArgs.add("--from-beginning");
        consoleConsumerArgs.add("--max-messages");
        consoleConsumerArgs.add("1");
        testCase.addFormatterConfig(consoleConsumerArgs, schemaregistry, eem);
        ConsoleConsumerOptions consumerOptions = new ConsoleConsumerOptions(consoleConsumerArgs.toArray(new String[0]));

        // run kafka-console-consumer.sh with the custom formatter
        final PrintStream DEFAULT_SYSTEM_OUT = System.out;
        try (
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PrintStream output = new PrintStream(out);
        )
        {
            // capture the output in a custom stream
            System.setOut(output);
            ConsoleConsumer.run(consumerOptions);

            // verify the output against the test case
            String formattedOutput = out.toString();
            assertEquals(testCase.getExpectedFormattedOutput() + testCase.getSeparator(), formattedOutput);
        }
        finally {
            // restore the default output stream
            System.setOut(DEFAULT_SYSTEM_OUT);
        }

        // clean up topic so events won't be consumed in subsequent tests
        try (AdminClient admin = AdminClient.create(producerProps)) {
            admin.deleteTopics(Collections.singletonList(topic)).all().get();
        }
    }


    private static void runConfigErrorTest(TestCase testCase, String expectedConfigError) throws IOException, InterruptedException, ExecutionException {
        try {
            runTest(testCase);
            fail("Config exception should have been thrown before the test case completed");
        }
        catch (ConfigException ce) {
            assertEquals(expectedConfigError, ce.getMessage());
        }
    }
}
