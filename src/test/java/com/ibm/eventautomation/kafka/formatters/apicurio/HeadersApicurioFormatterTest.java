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

import static io.apicurio.registry.serde.SerdeConfig.USE_ID;
import static io.apicurio.registry.serde.SerdeConfig.ENABLE_HEADERS;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import com.ibm.eventautomation.kafka.schemas.SampleRecord;

import io.apicurio.registry.serde.config.IdOption;

public class HeadersApicurioFormatterTest extends ApicurioFormatterTest<SampleRecord> {

    private static final String SEPARATOR = "\n^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n";

    @Override
    public SampleRecord createRecord() {
        return new SampleRecord(
            "da5f031f-703c-4197-a852-c85214595642",
            "New name",
            24.19,
            9);
    }

    @Override
    public String getExpectedFormattedOutput() {
        return
            "{" +
                "\"id\": \"da5f031f-703c-4197-a852-c85214595642\", " +
                "\"name\": \"New name\", " +
                "\"price\": 24.19, " +
                "\"quantity\": 9" +
            "}";
    }

    @Override
    public String getSeparator() {
        return SEPARATOR;
    }

    @Override
    public void setApicurioProducerProperties(Properties props) {
        props.put(USE_ID, IdOption.contentId.name());
        props.put(ENABLE_HEADERS, true);
    }

    @Override
    public String createApicurioFormatterPropertiesFile(Map<String, String> props) throws IOException {
        props.put(USE_ID, IdOption.contentId.name());
        props.put(ENABLE_HEADERS, "true");
        return createPropertiesFile(props);
    }
}
