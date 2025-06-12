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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.config.ConfigException;


/**
 * Formatter for Avro-encoded Kafka events where the schema can be retrieved
 *  from an instance of Event Endpoint Management.
 */
public class EEMAvroFormatter extends AbstractAvroFormatter {

    private static final String CONFIG_EEM_API_TOKEN      = "eem.token";
    private static final String CONFIG_EEM_API_ENDPOINT   = "eem.endpoint";
    private static final String CONFIG_EEM_API_TRUSTSTORE = "eem.truststore";
    private static final String CONFIG_EEM_API_INSECURE   = "eem.skipcertificatevalidation";

    private EEMApiClient eemApiClient = null;

    private DatumReader<GenericRecord> datumReader = null;
    private BinaryDecoder decoder = null;

    /**
     * Configures the MessageFormatter
     *
     * @param configs Map to configure the formatter
     */
    @Override
    public void configure(Map<String, ?> configs) {
        super.configure(configs);

        if (!configs.containsKey(CONFIG_EEM_API_TOKEN) || !configs.containsKey(CONFIG_EEM_API_ENDPOINT)) {
            throw new ConfigException(CONFIG_EEM_API_TOKEN + " and " + CONFIG_EEM_API_ENDPOINT + " are required properties");
        }

        String eemApiAccessToken = (String) configs.get(CONFIG_EEM_API_TOKEN);
        String eemApiEndpoint = (String) configs.get(CONFIG_EEM_API_ENDPOINT);

        if (eemApiAccessToken == null || eemApiAccessToken.isEmpty()) {
            throw new ConfigException(CONFIG_EEM_API_TOKEN + " cannot be null or empty");
        }
        if (eemApiEndpoint == null || eemApiEndpoint.isEmpty()) {
            throw new ConfigException(CONFIG_EEM_API_ENDPOINT + " cannot be null or empty");
        }

        boolean skipEemApiCertificateValidation =
            configs.containsKey(CONFIG_EEM_API_INSECURE) &&
            Boolean.parseBoolean(configs.get(CONFIG_EEM_API_INSECURE).toString());

        File eemCustomTrustStore = null;
        if (configs.containsKey(CONFIG_EEM_API_TRUSTSTORE)) {
            String truststoreLocation = configs.get(CONFIG_EEM_API_TRUSTSTORE).toString();
            File truststoreFile = new File(truststoreLocation);
            if (truststoreFile.exists() &&
                truststoreFile.isFile() &&
                truststoreFile.canRead() &&
                truststoreLocation.endsWith(".pem"))
            {
                eemCustomTrustStore = truststoreFile;
            }
            else {
                throw new ConfigException(CONFIG_EEM_API_TRUSTSTORE + " should be " +
                    "the location of a PEM file containing the certificate " +
                    "authority for the Event Endpoint Manager API");
            }
        }

        eemApiClient = new EEMApiClient(eemApiEndpoint,
                                        eemApiAccessToken,
                                        eemCustomTrustStore,
                                        skipEemApiCertificateValidation);
    }


    @Override
    protected Object deserialize(ConsumerRecord<byte[], byte[]> record) throws IOException {
        if (decoder == null) {
            prepareTopicDecoder(record.topic());
        }

        return avroDeserialize(record, decoder, datumReader);
    }


    private void prepareTopicDecoder(String eemTopic) {
        String schemaDefinition = eemApiClient.getAvroSchema(eemTopic);

        Schema schema = new Schema.Parser().parse(schemaDefinition);
        datumReader = new GenericDatumReader<>(schema);

        ByteArrayInputStream initInput = new ByteArrayInputStream(new byte[0]);
        decoder = DecoderFactory.get().directBinaryDecoder(initInput, null);
    }
}
