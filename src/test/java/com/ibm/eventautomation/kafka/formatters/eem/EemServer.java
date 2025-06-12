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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.mockserver.client.MockServerClient;
import org.mockserver.configuration.Configuration;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.socket.PortFactory;

/**
 * Test instance of Event Endpoint Management, using a hard-coded mock API
 *  response from a local file.
 */
public class EemServer {

    private MockServerClient eemServerClient;
    private ClientAndServer eemServer;
    private int port;
    private File eemServerCaFile;


    public void start() throws IOException {
        String mockApiResponseFile = getClass().getClassLoader().getResource("eem/mock-api-response.json").getFile();
        String mockApiResponse = new String(Files.readAllBytes(Paths.get(mockApiResponseFile)));

        port = PortFactory.findFreePort();

        Configuration eemServerConfig = new Configuration();
        eemServer = ClientAndServer.startClientAndServer(eemServerConfig, port);
        eemServerClient = new MockServerClient(eemServerConfig, "localhost", port);

        eemServerClient.when(
            org.mockserver.model.HttpRequest
                .request()
                    .withMethod("GET")
                    .withPath("/admin/eventendpoints")
        ).respond(
            org.mockserver.model.HttpResponse.response()
                .withStatusCode(200)
                .withHeader("Content-Type", "application/json")
                .withBody(mockApiResponse)
        );

        eemServerCaFile = File.createTempFile("mock-eem-ca", ".pem");
        eemServerCaFile.deleteOnExit();
        try (
            InputStream is = getClass().getResourceAsStream("/org/mockserver/socket/CertificateAuthorityCertificate.pem");
            OutputStream os = new FileOutputStream(eemServerCaFile);
        )
        {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        }
    }

    public void stop() {
        eemServer.stop();
    }

    public String getAdminApiAddress() {
        return String.format("https://%s:%s/admin", "localhost", port);
    }

    /**
     * Returns the location of the CA used by the HTTPS APIs for the mock EEM server.
     */
    public File getCA() throws IOException {
        return eemServerCaFile;
    }
}
