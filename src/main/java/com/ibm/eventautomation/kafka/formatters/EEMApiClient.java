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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.GeneralSecurityException;

import javax.net.ssl.HttpsURLConnection;

import org.apache.kafka.common.config.ConfigException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.eventautomation.utils.HttpsURLConnectionTrustUpdater;

public class EEMApiClient {

    private final Logger log = LoggerFactory.getLogger(EEMApiClient.class);

    private final String eemApiEndpoint;
    private final String eemApiAccessToken;
    private final File eemCustomTrustStore;
    private final boolean skipEemApiCertificateValidation;


    public EEMApiClient(String endpoint, String apitoken, File apiCertAuth, boolean skipApiCertValidation) {
        this.eemApiEndpoint = endpoint;
        this.eemApiAccessToken = apitoken;
        this.eemCustomTrustStore = apiCertAuth;
        this.skipEemApiCertificateValidation = skipApiCertValidation;
    }

    public String getAvroSchema(String eemTopic) throws ConfigException {
        log.debug("Getting event endpoints from Event Endpoint Management");

        String eventEndpointsApiResponse = apiHttpGet("/eventendpoints");
        JSONArray eventEndpoints = new JSONArray(eventEndpointsApiResponse);
        return getEndpointSchema(eventEndpoints, eemTopic);
    }



    private String apiHttpGet(String path) {
        HttpsURLConnection conn = null;
        try {
            URL url = new URL(eemApiEndpoint + path);
            conn = (HttpsURLConnection) url.openConnection();

            HttpsURLConnectionTrustUpdater sslUpdater = new HttpsURLConnectionTrustUpdater(skipEemApiCertificateValidation);
            if (eemCustomTrustStore != null) {
                sslUpdater.trustCustomCertAuthority(conn, eemCustomTrustStore);
            }
            else if (skipEemApiCertificateValidation) {
                sslUpdater.trustAnything(conn);
            }

            conn.setRequestProperty("Authorization", "Bearer " + eemApiAccessToken);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() != 200) {
                throw new ConfigException(conn.getResponseMessage());
            }

            return readResponseAsString(conn);
        }
        catch (javax.net.ssl.SSLHandshakeException e) {
            final String SSL_ERR = "SSL handshake exception when making API call " +
                "to retrieve a schema from Event Endpoint Management. " +
                "Set eem.skipcertificatevalidation to true to ignore this.";

            log.error(SSL_ERR, e);
            throw new ConfigException(SSL_ERR);
        }
        catch (IOException | GeneralSecurityException e) {
            log.error("Failed to prepare decoder", e);

            throw new ConfigException(e.getMessage());
        }
        finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }


    private String readResponseAsString(HttpsURLConnection conn) throws IOException {
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
        }
        return response.toString();
    }


    private String getEndpointSchema(JSONArray eventEndpoints, String topicAlias) {
        try {
            for (int i = 0; i < eventEndpoints.length(); i++) {
                JSONObject eventEndpoint = eventEndpoints.getJSONObject(i);
                JSONArray eventOptions = eventEndpoint.getJSONArray("options");
                for (int j = 0; j < eventOptions.length(); j++) {
                    JSONObject eventOption = eventOptions.getJSONObject(j);
                    if (topicAlias.equals(eventOption.getString("alias"))) {
                        JSONObject schemaDef = eventEndpoint.getJSONObject("schema");

                        if (!"avro".equals(schemaDef.getString("type"))) {
                            throw new ConfigException("Unsupported schema type: " + schemaDef.getString("type") + ". Only Avro schemas are supported.");
                        }
                        return schemaDef.getString("content");
                    }
                }
            }
        }
        catch (JSONException exc) {
            log.error("Unexpected API response from Event Endpoint Management", exc);
        }

        throw new ConfigException("Schema not available in Event Endpoint Management catalog");
    }
}
