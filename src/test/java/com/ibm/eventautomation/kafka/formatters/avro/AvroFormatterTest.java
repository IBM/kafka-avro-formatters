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

import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.GenericRecordBuilder;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import com.ibm.eventautomation.kafka.formatters.TestCase;
import com.ibm.eventautomation.kafka.formatters.apicurio.ApicurioContainer;
import com.ibm.eventautomation.kafka.formatters.eem.EemServer;


public abstract class AvroFormatterTest extends TestCase {

    private File schemaFile;
    private Schema schema;
    private GenericRecordBuilder builder;
    private DatumWriter<GenericRecord> writer;
    private BinaryEncoder encoder = null;


    public AvroFormatterTest() throws IOException {
        schemaFile = new File(getClass().getClassLoader().getResource(getSchemaFile()).getFile());
        schema = new Schema.Parser().parse(schemaFile);
        builder = new GenericRecordBuilder(schema);
        writer = new GenericDatumWriter<>(schema);
    }

    public abstract String getSchemaFile();

    public abstract GenericRecord createAvroRecord(Schema schema, GenericRecordBuilder builder);

    @Override
    public void produceTestEvent(String topic, Properties producerProperties, ApicurioContainer schemaRegistry) throws IOException
    {
        GenericRecord avroRecord = createAvroRecord(schema, builder);

        byte[] avroData;
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            encoder = EncoderFactory.get().directBinaryEncoder(output, encoder);
            writer.write(avroRecord, encoder);
            avroData = output.toByteArray();
        }

        producerProperties.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getCanonicalName());
        producerProperties.put(VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getCanonicalName());

        try (Producer<String, byte[]> producer = new KafkaProducer<>(producerProperties)) {
            producer.send(new ProducerRecord<>(topic, "key", avroData));
            producer.flush();
        }
    }

    @Override
    public void addFormatterConfig(List<String> consoleConsumerOptions, ApicurioContainer schemaRegistry, EemServer eem) throws IOException {
        Map<String, String> formatterProperties = new HashMap<>();
        formatterProperties.put("schema.file", schemaFile.getAbsolutePath());
        formatterProperties.put("line.separator", getSeparator().replaceAll("\n", "\\\\n"));

        consoleConsumerOptions.add("--formatter");
        consoleConsumerOptions.add("com.ibm.eventautomation.kafka.formatters.AvroFormatter");
        consoleConsumerOptions.add("--formatter-config");
        consoleConsumerOptions.add(createPropertiesFile(formatterProperties));
    }
}
