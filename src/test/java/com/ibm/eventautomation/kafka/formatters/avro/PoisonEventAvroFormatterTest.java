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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.GenericRecordBuilder;

import com.ibm.eventautomation.kafka.formatters.apicurio.ApicurioContainer;
import com.ibm.eventautomation.kafka.formatters.eem.EemServer;

public class PoisonEventAvroFormatterTest extends AvroFormatterTest {

    private static final String SEPARATOR = "\n**********\n";

    public PoisonEventAvroFormatterTest() throws IOException {
        super();
    }

    @Override
    public String getSeparator() {
        return SEPARATOR;
    }

    @Override
    public String getSchemaFile() {
        return "avro/simple.avro";
    }

    private String incorrectSchema = new File(getClass().getClassLoader().getResource("avro/orders.avro").getFile()).getAbsolutePath();

    @Override
    public void addFormatterConfig(List<String> consoleConsumerOptions, ApicurioContainer schemaRegistry, EemServer eem) throws IOException {
        Map<String, String> formatterProperties = new HashMap<>();
        formatterProperties.put("schema.file", incorrectSchema);
        formatterProperties.put("line.separator", getSeparator().replaceAll("\n", "\\\\n"));

        consoleConsumerOptions.add("--formatter");
        consoleConsumerOptions.add("com.ibm.eventautomation.kafka.formatters.AvroFormatter");
        consoleConsumerOptions.add("--formatter-config");
        consoleConsumerOptions.add(createPropertiesFile(formatterProperties));
    }

    @Override
    public String getExpectedFormattedOutput()
    {
        return "Unable to deserialize event (Malformed data. Length is negative: -16)";
    }

    @Override
    public GenericRecord createAvroRecord(Schema schema, GenericRecordBuilder builder) {
        return builder
            .set("id",       "4efb1831-d7fc-4665-8ff8-bce4e777b37f")
            .set("name",     "Test Item")
            .set("price",    47.99)
            .set("quantity", 2)
            .build();
    }

}
