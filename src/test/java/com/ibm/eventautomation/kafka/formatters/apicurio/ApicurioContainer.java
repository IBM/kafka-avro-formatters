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

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

/**
 * Test instance of Apicurio Registry, using in-memory storage.
 */
public class ApicurioContainer extends GenericContainer<ApicurioContainer> {

    private static final String IMAGE_NAME = "apicurio/apicurio-registry:latest";

    private static final int APICURIO_PORT = 8080;

    public ApicurioContainer() {
        super(IMAGE_NAME);
        withExposedPorts(APICURIO_PORT);
        waitingFor(Wait.forHttp("/health/ready"));
    }

    public String getRegistryAddress() {
        return String.format("http://%s:%s", getHost(), getMappedPort(APICURIO_PORT));
    }
}
