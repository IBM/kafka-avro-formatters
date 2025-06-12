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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.ibm.eventautomation.kafka.formatters.apicurio.ApicurioContainer;
import com.ibm.eventautomation.kafka.formatters.eem.EemServer;

public abstract class TestCase {

    public abstract void produceTestEvent(String topic, Properties producerProperties, ApicurioContainer schemaRegistry) throws IOException;

    public abstract void addFormatterConfig(List<String> consoleConsumerOptions, ApicurioContainer schemaRegistry, EemServer eem) throws IOException;

    public abstract String getSeparator();

    public abstract String getExpectedFormattedOutput();


    /**
     * Creates a properties file containing the provided config, and returns the location of
     *  the file.
     *
     * File is temporary and will be deleted when the test completes.
     */
    public String createPropertiesFile(Map<String, String> properties) throws IOException {
        File formatterProperties = File.createTempFile("formatter", ".properties");
        formatterProperties.deleteOnExit();
        try (FileWriter fw = new FileWriter(formatterProperties)) {
            for (String key : properties.keySet()) {
                fw.write(key + "=" + properties.get(key) + "\n");
            }
        }
        return formatterProperties.getAbsolutePath();
    }

    public String getTopicName(int optionalSuffix) {
        return "TEST." + optionalSuffix;
    }
}
