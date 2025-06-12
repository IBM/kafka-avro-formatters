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
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import com.ibm.eventautomation.kafka.schemas.BillingAddress;
import com.ibm.eventautomation.kafka.schemas.BillingCountry;
import com.ibm.eventautomation.kafka.schemas.Customer;
import com.ibm.eventautomation.kafka.schemas.OnlineAddress;
import com.ibm.eventautomation.kafka.schemas.OnlineOrder;
import com.ibm.eventautomation.kafka.schemas.ShippingAddress;
import com.ibm.eventautomation.kafka.schemas.ShippingCountry;

import io.apicurio.registry.serde.config.IdOption;

public class OrderApicurioFormatterTest extends ApicurioFormatterTest<OnlineOrder> {

    private static final String SEPARATOR = "\n\n.\n\n";


    @Override
    public OnlineOrder createRecord() {
        Customer customer = new Customer(
                                    UUID.fromString("91c58b3a-a084-4ddd-880d-4d6d328eef56"),
                                    "Granville Kreiger",
                                    List.of("granville.kreiger@example.com"));
        List<CharSequence> products = List.of(
                                    "L Blue High-waist Jeans",
                                    "L Black High-waist Jeans",
                                    "M Navy Low-rise Jeans");
        ShippingAddress shippingAddress = new ShippingAddress(
                                    66,
                                    "Flatley Ferry",
                                    "East Wynonaberg",
                                    "24226-5601",
                                    new ShippingCountry("US", "United States"),
                                    List.of("(401) 732-2358", "845-862-5527"));
        BillingAddress billingAddress = new BillingAddress(
                                    668,
                                    "Gregory Crossing",
                                    "Faheyhaven",
                                    "59069",
                                    new BillingCountry("US", "United States"),
                                    List.of("(802) 512-2359"));

        return new OnlineOrder(UUID.fromString("e7a31e68-0988-4c6b-82c5-b13b82bfd57a"),
                               customer,
                               products,
                               new OnlineAddress(shippingAddress, billingAddress));
    }

    @Override
    public String getExpectedFormattedOutput() {
        return
            "{" +
                "\"id\": \"e7a31e68-0988-4c6b-82c5-b13b82bfd57a\", " +
                "\"customer\": {" +
                    "\"id\": \"91c58b3a-a084-4ddd-880d-4d6d328eef56\", " +
                    "\"name\": \"Granville Kreiger\", " +
                    "\"emails\": [" +
                        "\"granville.kreiger@example.com\"" +
                    "]" +
                "}, " +
                "\"products\": [" +
                    "\"L Blue High-waist Jeans\", " +
                    "\"L Black High-waist Jeans\", " +
                    "\"M Navy Low-rise Jeans\"" +
                "], " +
                "\"address\": {" +
                    "\"shippingaddress\": {" +
                        "\"number\": 66, " +
                        "\"street\": \"Flatley Ferry\", " +
                        "\"city\": \"East Wynonaberg\", " +
                        "\"zipcode\": \"24226-5601\", " +
                        "\"country\": {" +
                            "\"code\": \"US\", " +
                            "\"name\": \"United States\"" +
                        "}, " +
                        "\"phones\": [" +
                            "\"(401) 732-2358\", " +
                            "\"845-862-5527\"" +
                        "]" +
                    "}, " +
                    "\"billingaddress\": {" +
                        "\"number\": 668, " +
                        "\"street\": \"Gregory Crossing\", " +
                        "\"city\": \"Faheyhaven\", " +
                        "\"zipcode\": \"59069\", " +
                        "\"country\": {" +
                            "\"code\": \"US\", " +
                            "\"name\": \"United States\"" +
                        "}, " +
                        "\"phones\": [" +
                            "\"(802) 512-2359\"" +
                        "]" +
                    "}" +
                "}" +
            "}";
    }

    @Override
    public String getSeparator() {
        return SEPARATOR;
    }

    @Override
    public void setApicurioProducerProperties(Properties props) {
        props.put(USE_ID, IdOption.globalId.name());
        props.put(ENABLE_HEADERS, true);
    }

    @Override
    public String createApicurioFormatterPropertiesFile(Map<String, String> props) throws IOException {
        props.put(USE_ID, IdOption.globalId.name());
        props.put(ENABLE_HEADERS, "true");
        return createPropertiesFile(props);
    }
}
