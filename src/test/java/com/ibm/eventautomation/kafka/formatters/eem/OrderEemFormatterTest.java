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
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.GenericRecordBuilder;

import com.ibm.eventautomation.kafka.formatters.apicurio.ApicurioContainer;
import com.ibm.eventautomation.kafka.formatters.avro.AvroFormatterTest;

public class OrderEemFormatterTest extends AvroFormatterTest {

    private static final String SEPARATOR = "\n";

    private final boolean skipSslCertValidation;

    public OrderEemFormatterTest(boolean skipSslCertValidation) throws IOException {
        super();

        this.skipSslCertValidation = skipSslCertValidation;
    }

    @Override
    public GenericRecord createAvroRecord(Schema schema, GenericRecordBuilder builder)
    {
        GenericRecord customer = new GenericData.Record(schema.getField("customer").schema());
        customer.put("id", "1cbe82b9-ad8c-4dfe-a40c-ef9317df0a27");
        customer.put("name", "Milford Rutherford");
        customer.put("emails", List.of("milford.rutherford@example.com"));

        List<String> products = List.of(
            "M Stonewashed Jogger Jeans",
            "M Black Crochet Jeans",
            "S Black High-waist Jeans",
            "M Black Crochet Jeans",
            "M Denim Capri Jeans");

        GenericRecord unitedStates = new GenericData.Record(schema
            .getField("address").schema()
            .getField("shippingaddress").schema()
            .getField("country").schema());
        unitedStates.put("code", "US");
        unitedStates.put("name", "United States");

        GenericRecord shippingAddress = new GenericData.Record(schema
            .getField("address").schema()
            .getField("shippingaddress").schema());
        shippingAddress.put("number", 337);
        shippingAddress.put("street", "Holley Isle");
        shippingAddress.put("city", "South Erminiamouth");
        shippingAddress.put("zipcode", "81178");
        shippingAddress.put("country", unitedStates);
        shippingAddress.put("phones", null);

        GenericRecord billingAddress = new GenericData.Record(schema
            .getField("address").schema()
            .getField("billingaddress").schema());
        billingAddress.put("number", 337);
        billingAddress.put("street", "Holley Isle");
        billingAddress.put("city", "South Erminiamouth");
        billingAddress.put("zipcode", "81178");
        billingAddress.put("country", unitedStates);
        billingAddress.put("phones", null);

        GenericRecord address = new GenericData.Record(schema.getField("address").schema());
        address.put("shippingaddress", shippingAddress);
        address.put("billingaddress", billingAddress);

        return builder
            .set("id",       "b7ac801c-e953-4e48-b928-e20b70a1fd8f")
            .set("customer", customer)
            .set("products", products)
            .set("address", address)
            .build();
    }

    @Override
    public String getExpectedFormattedOutput()
    {
        return
            "{" +
                "\"id\": \"b7ac801c-e953-4e48-b928-e20b70a1fd8f\", " +
                "\"customer\": {" +
                    "\"id\": \"1cbe82b9-ad8c-4dfe-a40c-ef9317df0a27\", " +
                    "\"name\": \"Milford Rutherford\", " +
                    "\"emails\": [" +
                        "\"milford.rutherford@example.com\"" +
                    "]" +
                "}, " +
                "\"products\": [" +
                    "\"M Stonewashed Jogger Jeans\", " +
                    "\"M Black Crochet Jeans\", " +
                    "\"S Black High-waist Jeans\", " +
                    "\"M Black Crochet Jeans\", " +
                    "\"M Denim Capri Jeans\"" +
                "], " +
                "\"address\": {" +
                    "\"shippingaddress\": {" +
                        "\"number\": 337, " +
                        "\"street\": \"Holley Isle\", " +
                        "\"city\": \"South Erminiamouth\", " +
                        "\"zipcode\": \"81178\", " +
                        "\"country\": {" +
                            "\"code\": \"US\", " +
                            "\"name\": \"United States\"" +
                        "}, " +
                        "\"phones\": null" +
                    "}, " +
                    "\"billingaddress\": {" +
                        "\"number\": 337, " +
                        "\"street\": \"Holley Isle\", " +
                        "\"city\": \"South Erminiamouth\", " +
                        "\"zipcode\": \"81178\", " +
                        "\"country\": {" +
                            "\"code\": \"US\", " +
                            "\"name\": \"United States\"" +
                        "}, " +
                        "\"phones\": null" +
                    "}" +
                "}" +
            "}";
    }


    @Override
    public void addFormatterConfig(List<String> consoleConsumerOptions, ApicurioContainer schemaRegistry, EemServer eem) throws IOException {
        Map<String, String> formatterProperties = new HashMap<>();
        formatterProperties.put("line.separator", getSeparator().replaceAll("\n", "\\\\n"));
        formatterProperties.put("eem.endpoint", eem.getAdminApiAddress());
        formatterProperties.put("eem.token", "00000000-0000-0000-0000-000000000000");
        if (skipSslCertValidation) {
            formatterProperties.put("eem.skipcertificatevalidation", "true");
        }
        else {
            formatterProperties.put("eem.truststore", eem.getCA().getAbsolutePath());
        }

        consoleConsumerOptions.add("--formatter");
        consoleConsumerOptions.add("com.ibm.eventautomation.kafka.formatters.EEMAvroFormatter");
        consoleConsumerOptions.add("--formatter-config");
        consoleConsumerOptions.add(createPropertiesFile(formatterProperties));
    }


    /**
     * Cannot use a temporary/dynamic topic name for EEM tests, as the topic
     *  name needs to match the API response used to lookup schemas.
     */
    @Override
    public String getTopicName(int optionalSuffix) {
        return "ORDERS.ONLINE";
    }

    @Override
    public String getSeparator() {
        return SEPARATOR;
    }

    @Override
    public String getSchemaFile() {
        return "avro/orders.avro";
    }
}
