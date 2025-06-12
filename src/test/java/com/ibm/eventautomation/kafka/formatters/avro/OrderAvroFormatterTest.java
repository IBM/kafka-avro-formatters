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
package com.ibm.eventautomation.kafka.formatters.avro;

import java.io.IOException;
import java.util.List;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.GenericRecordBuilder;

public class OrderAvroFormatterTest extends AvroFormatterTest {

    private static final String SEPARATOR = "\n---------------------------------------------------\n";

    public OrderAvroFormatterTest() throws IOException {
        super();
    }

    @Override
    public String getSeparator() {
        return SEPARATOR;
    }

    @Override
    public String getSchemaFile() {
        return "avro/orders.avro";
    }

    @Override
    public GenericRecord createAvroRecord(Schema schema, GenericRecordBuilder builder) {
        GenericRecord customer = new GenericData.Record(schema.getField("customer").schema());
        customer.put("id", "08c7bd5d-a9a4-434f-a09a-8874a1f7c1fc");
        customer.put("name", "Joe Bloggs");
        customer.put("emails", List.of("joe.bloggs@personal.com", "joe.bloggs@work.com" ));

        List<String> products = List.of("product1", "product2", "product3");

        GenericRecord unitedStates = new GenericData.Record(schema
            .getField("address").schema()
            .getField("shippingaddress").schema()
            .getField("country").schema());
        unitedStates.put("code", "US");
        unitedStates.put("name", "United States");

        GenericRecord shippingAddress = new GenericData.Record(schema
            .getField("address").schema()
            .getField("shippingaddress").schema());
        shippingAddress.put("number", 1);
        shippingAddress.put("street", "Street Name");
        shippingAddress.put("city", "City");
        shippingAddress.put("zipcode", "12345");
        shippingAddress.put("country", unitedStates);
        shippingAddress.put("phones", List.of("(508) 217-7740", "660.503.7376" ));

        GenericRecord billingAddress = new GenericData.Record(schema
            .getField("address").schema()
            .getField("billingaddress").schema());
        billingAddress.put("number", 378);
        billingAddress.put("street", "Pierre Rapid");
        billingAddress.put("city", "East Rachel");
        billingAddress.put("zipcode", "81824");
        billingAddress.put("country", unitedStates);
        billingAddress.put("phones", null);

        GenericRecord address = new GenericData.Record(schema.getField("address").schema());
        address.put("shippingaddress", shippingAddress);
        address.put("billingaddress", billingAddress);

        return builder
            .set("id",       "7ddd59d6-5a82-40ba-8fed-56f284051a11")
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
                "\"id\": \"7ddd59d6-5a82-40ba-8fed-56f284051a11\", " +
                "\"customer\": {" +
                    "\"id\": \"08c7bd5d-a9a4-434f-a09a-8874a1f7c1fc\", " +
                    "\"name\": \"Joe Bloggs\", " +
                    "\"emails\": [" +
                        "\"joe.bloggs@personal.com\", " +
                        "\"joe.bloggs@work.com\"" +
                    "]" +
                "}, " +
                "\"products\": [" +
                    "\"product1\", " +
                    "\"product2\", " +
                    "\"product3\"" +
                "], " +
                "\"address\": {" +
                    "\"shippingaddress\": {" +
                        "\"number\": 1, " +
                        "\"street\": \"Street Name\", " +
                        "\"city\": \"City\", " +
                        "\"zipcode\": \"12345\", " +
                        "\"country\": {" +
                            "\"code\": \"US\", " +
                            "\"name\": \"United States\"" +
                        "}, " +
                        "\"phones\": [" +
                            "\"(508) 217-7740\", " +
                            "\"660.503.7376\"" +
                        "]" +
                    "}, " +
                    "\"billingaddress\": {" +
                        "\"number\": 378, " +
                        "\"street\": \"Pierre Rapid\", " +
                        "\"city\": \"East Rachel\", " +
                        "\"zipcode\": \"81824\", " +
                        "\"country\": {" +
                            "\"code\": \"US\", " +
                            "\"name\": \"United States\"" +
                        "}, " +
                        "\"phones\": null" +
                    "}" +
                "}" +
            "}";
    }
}
