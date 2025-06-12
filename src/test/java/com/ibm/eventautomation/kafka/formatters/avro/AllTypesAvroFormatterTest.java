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
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.GenericRecordBuilder;
import org.apache.avro.generic.GenericData.Fixed;

public class AllTypesAvroFormatterTest extends AvroFormatterTest {

    private static final String SEPARATOR = "\n~~ ** ~~ ** ~~ ** ~~ ** ~~ ** ~~ ** ~~\n";

    public AllTypesAvroFormatterTest() throws IOException {
        super();
    }

    @Override
    public String getSeparator() {
        return SEPARATOR;
    }

    @Override
    public String getSchemaFile() {
        return "avro/types.avro";
    }

    @Override
    public GenericRecord createAvroRecord(Schema schema, GenericRecordBuilder builder) {
        return builder
            .set("val1",  true)
            .set("val2",  123)
            .set("val3",  45L)
            .set("val4",  1.23f)
            .set("val5",  8.23d)
            .set("val6",  ByteBuffer.wrap("abc".getBytes()))
            .set("val7",  "abc")
            .set("val8",  new GenericData.EnumSymbol(schema.getField("val8").schema(), "B"))
            .set("val9",  List.of("A", "B", "C"))
            .set("val10", List.of(true, false, true))
            .set("val11", List.of(1, 2, 3))
            .set("val12", Map.of("first", "A", "second", "B", "third", "C"))
            .set("val13", Map.of("A", 1, "B", 2, "C", 3))
            .set("val14", Map.of("A", true, "B", false, "C", true))
            .set("val15", createFixedByteValue(schema.getField("val16").schema()))
            .set("val16", "abc")
            .set("val17", createObjectList(schema.getField("val17").schema().getElementType()))
            .build();
    }

    private Fixed createFixedByteValue(Schema schema) {
        byte[] bytes = new byte[0];
        try {
            bytes = MessageDigest.getInstance("MD5").digest("abc".getBytes());
        }
        catch (NoSuchAlgorithmException nsae) {}
        return new GenericData.Fixed(schema, bytes);
    }

    private List<GenericRecord> createObjectList(Schema schema) {
        GenericRecordBuilder innerBuilder = new GenericRecordBuilder(schema);
        return List.of(
            innerBuilder
                .set("val17a", "FIRST")
                .set("val17b", "SECOND")
                .build(),
            innerBuilder
                .set("val17a", "first")
                .set("val17b", "second")
                .build()
        );
    }

    @Override
    public String getExpectedFormattedOutput()
    {
        return
            "{" +
                "\"val1\": true, " +
                "\"val2\": 123, " +
                "\"val3\": 45, " +
                "\"val4\": 1.23, " +
                "\"val5\": 8.23, " +
                "\"val6\": \"abc\", " +
                "\"val7\": \"abc\", " +
                "\"val8\": \"B\", " +
                "\"val9\": [\"A\", \"B\", \"C\"], " +
                "\"val10\": [true, false, true], " +
                "\"val11\": [1, 2, 3], " +
                "\"val12\": {\"third\": \"C\", \"first\": \"A\", \"second\": \"B\"}, " +
                "\"val13\": {\"A\": 1, \"B\": 2, \"C\": 3}, " +
                "\"val14\": {\"A\": true, \"B\": false, \"C\": true}, " +
                "\"val15\": [-112, 1, 80, -104, 60, -46, 79, -80, -42, -106, 63, 125, 40, -31, 127, 114], " +
                "\"val16\": \"abc\", " +
                "\"val17\": [" +
                    "{\"val17a\": \"FIRST\", \"val17b\": \"SECOND\"}, " +
                    "{\"val17a\": \"first\", \"val17b\": \"second\"}" +
                "]" +
            "}";
    }
}
