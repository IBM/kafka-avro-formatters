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
package com.ibm.eventautomation.kafka.formatters.eem;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.GenericRecordBuilder;

import com.ibm.eventautomation.kafka.formatters.apicurio.ApicurioContainer;
import com.ibm.eventautomation.kafka.formatters.avro.AvroFormatterTest;

public class EemFormatterMissingTopicTest extends AvroFormatterTest {

    private static final String SEPARATOR = "\n----...----\n";

    public EemFormatterMissingTopicTest() throws IOException {
        super();
    }

    @Override
    public GenericRecord createAvroRecord(Schema schema, GenericRecordBuilder builder)
    {
        return builder
            .set("id",       "a")
            .set("name",     "b")
            .set("price",    1.0)
            .set("quantity", 1)
            .build();
    }

    @Override
    public void addFormatterConfig(List<String> consoleConsumerOptions, ApicurioContainer schemaRegistry, EemServer eem) throws IOException {
        Map<String, String> formatterProperties = new HashMap<>();
        formatterProperties.put("line.separator", getSeparator().replaceAll("\n", "\\\\n"));
        formatterProperties.put("eem.endpoint", eem.getAdminApiAddress());
        formatterProperties.put("eem.token", "00000000-0000-0000-0000-000000000000");
        formatterProperties.put("eem.truststore", eem.getCA().getAbsolutePath());

        consoleConsumerOptions.add("--formatter");
        consoleConsumerOptions.add("com.ibm.eventautomation.kafka.formatters.EEMAvroFormatter");
        consoleConsumerOptions.add("--formatter-config");
        consoleConsumerOptions.add(createPropertiesFile(formatterProperties));
    }

    @Override
    public String getExpectedFormattedOutput()
    {
        return null;
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
