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
import static io.apicurio.registry.serde.SerdeConfig.ID_HANDLER;
import static io.apicurio.registry.serde.SerdeConfig.ENABLE_HEADERS;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import com.ibm.eventautomation.kafka.schemas.SampleRecord;

import io.apicurio.registry.serde.Legacy4ByteIdHandler;
import io.apicurio.registry.serde.config.IdOption;

public class MagicByteApicurioFormatterTest extends ApicurioFormatterTest<SampleRecord> {

    private static final String SEPARATOR = "";

    @Override
    public SampleRecord createRecord() {
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
    public void setApicurioProducerProperties(Properties props) {
        props.put(USE_ID, IdOption.contentId.name());
        props.put(ENABLE_HEADERS, false);
        props.put(ID_HANDLER, Legacy4ByteIdHandler.class.getCanonicalName());
    }

    @Override
    public String createApicurioFormatterPropertiesFile(Map<String, String> props) throws IOException {
        props.put(USE_ID, IdOption.contentId.name());
        props.put(ENABLE_HEADERS, "false");
        props.put(ID_HANDLER, Legacy4ByteIdHandler.class.getCanonicalName());
        return createPropertiesFile(props);
    }
}
