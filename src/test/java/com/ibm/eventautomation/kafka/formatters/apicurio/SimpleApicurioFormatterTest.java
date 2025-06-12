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

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import com.ibm.eventautomation.kafka.schemas.SampleRecord;

public class SimpleApicurioFormatterTest extends ApicurioFormatterTest<SampleRecord> {

    private static final String SEPARATOR = "\n\n";

    @Override
    public SampleRecord createRecord() {
        return new SampleRecord(
            "e48b3333-a100-4bae-a0fb-b49271d7061f",
            "Test name",
            9.87,
            4);
    }

    @Override
    public String getExpectedFormattedOutput() {
        return
            "{" +
                "\"id\": \"e48b3333-a100-4bae-a0fb-b49271d7061f\", " +
                "\"name\": \"Test name\", " +
                "\"price\": 9.87, " +
                "\"quantity\": 4" +
            "}";
    }

    @Override
    public String getSeparator() {
        return SEPARATOR;
    }

    @Override
    public void setApicurioProducerProperties(Properties props) { }

    @Override
    public String createApicurioFormatterPropertiesFile(Map<String, String> props) throws IOException {
        return createPropertiesFile(props);
    }
}
