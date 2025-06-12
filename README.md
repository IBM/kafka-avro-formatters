# Avro Console formatters

**Parse and format the contents of [Apache Avro](https://avro.apache.org/)™ encoded events when using `kafka-console-consumer.sh`**

It can be used with:
- Avro schemas stored as [local files](#schemas-in-local-files)
- Avro schemas stored in an [Apicurio Registry](#schemas-in-schema-registries)
- Avro schemas stored in an IBM [Event Endpoint Management Catalog](#event-endpoint-management)

## Instructions for use

1. Download the avro-formatters jar from the Releases page
2. Add the location of the jar to a `CLASSPATH` environment variable
3. Add formatter config options to your `kafka-console-consumer.sh` command

For example:
```
export CLASSPATH=avro-formatters-0.1.0.jar

kafka-console-consumer.sh \
  --bootstrap-server  kafka-bootstrap:9092 \
  --topic             YOUR.TOPIC \
  --consumer.config   consumer.properties \
  --formatter         com.ibm.eventautomation.kafka.formatters.AvroFormatter \
  --formatter-config  avro-formatter.properties
```

The correct values for `--formatter` and `--formatter-config` will depend on where your Apache Avro schema should be retrieved from.

### Schemas in local files

**If your Avro schema is available in a local file**, add the following config options when you run `kafka-console-consumer.sh` :

| **option**           | **value**                                                |
| -------------------- | -------------------------------------------------------- |
| `--formatter`        | `com.ibm.eventautomation.kafka.formatters.AvroFormatter` |
| `--formatter-config` | `avro-formatter.properties`                              |

In your `avro-formatter.properties` file, you should include:

|                  | **required?** | **explanation**                                                                      |
| ---------------- | ------------- | ------------------------------------------------------------------------------------ |
| `schema.file`    | yes           | Location of your Avro schema file                                                    |
| `id.num.bytes`   | no            | The number of bytes used at the start of the event payloads to store a schema ID (e.g. 4 or 8). <br>This defaults to 0 if the config option is not set, which is means the schema ID is not stored in the event payload. |
| `line.separator` | no            | String to print between each Kafka message. (e.g. `\n` to separate messages with newlines).<br>If not set, no separator is printed between messages. |

For example:

```
kafka-console-consumer.sh \
  --bootstrap-server  my-kafka-cluster-bootstrap:9092 \
  --topic             MY.AVRO.TOPIC \
  --consumer.config   consumer.properties \
  --formatter         com.ibm.eventautomation.kafka.formatters.AvroFormatter \
  --formatter-config  avro-formatter.properties
```

`avro-formatter.properties`
```
schema=schema.avro
line.separator=\n------------------------------------------------\n
```

### Schemas in schema registries

**If your Avro schema is stored in an Apicurio schema registry**, add the following config options when you run `kafka-console-consumer.sh` :

| **option**           | **value**                                                    |
| -------------------- | ------------------------------------------------------------ |
| `--formatter`        | `com.ibm.eventautomation.kafka.formatters.ApicurioFormatter` |
| `--formatter-config` | `apicurio-formatter.properties`                              |

In your `apicurio-formatter.properties` file, you should include `AvroKafkaDeserializer` config options, as described in [Apicurio Registry documentation](https://www.apicur.io/registry/docs/apicurio-registry/2.6.x/getting-started/assembly-configuring-kafka-client-serdes.html)

You can also include a custom `line.separator` config option, containing a string to print between messages.

For example:

```
kafka-console-consumer.sh \
  --bootstrap-server  my-kafka-cluster-bootstrap:9092 \
  --topic             MY.APICURIO.TOPIC \
  --consumer.config   consumer.properties \
  --formatter         com.ibm.eventautomation.kafka.formatters.ApicurioFormatter \
  --formatter-config  apicurio-formatter.properties
```

`apicurio-formatter.properties`
```
apicurio.registry.url=https://my-apicurio-registry
apicurio.auth.username=my-username
apicurio.auth.password=my-schema-registry-password
apicurio.rest.request.ssl.truststore.location=ca-cert.p12
apicurio.rest.request.ssl.truststore.type=PKCS12
apicurio.rest.request.ssl.truststore.password=ca-password
line.separator=\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n
```

### Event Endpoint Management

**If your Avro schema is available in the IBM Event Endpoint Management catalog**, add the following config options when you run `kafka-console-consumer.sh` :


| **option**           | **value**                                                   |
| -------------------- | ----------------------------------------------------------- |
| `--formatter`        | `com.ibm.eventautomation.kafka.formatters.EEMAvroFormatter` |
| `--formatter-config` | `eem-formatter.properties`                                  |

In your `eem-formatter.properties` file, you should include:

|                                 | **required?** | **explanation**                               |
| ------------------------------- | ------------- | --------------------------------------------- |
| `eem.endpoint`                  | yes           | Event Endpoint Management Admin API URL <br>(_displayed in your Profile page_)      |
| `eem.token`                     | yes           | Event Endpoint Management [API access token](https://ibm.github.io/event-automation/eem/security/api-tokens/#api-access-tokens) |
| `eem.truststore`                | no            | Location of a PEM file containing the certificate authority for the Event Endpoint Management Admin API |
| `eem.skipcertificatevalidation` | no            | Set this to `true` if you want to ignore skip certificate validation for API calls to the Event Endpoint Management. <br>(If a custom truststore is provided, setting this to true will only skip hostname validation.) |
| `id.num.bytes`                  | no            | The number of bytes used at the start of the event payloads to store a schema ID (e.g. 4 or 8). <br>This defaults to 0 if the config option is not set, which is means the schema ID is not stored in the event payload. |
| `line.separator`                | no            | String to print between each Kafka message. (e.g. `\n` to separate messages with newlines).<br>If not set, no separator is printed between messages. |

For example:

```
kafka-console-consumer.sh \
  --bootstrap-server  my-eem-gateway:443 \
  --topic             MY.EEM.TOPIC \
  --consumer.config   credentials.properties \
  --formatter         com.ibm.eventautomation.kafka.formatters.EEMAvroFormatter \
  --formatter-config  eem-formatter.properties
```

`eem-formatter.properties`
```
eem.endpoint=https://eem.my-eem-manager-ibm-eem-server-event-automation.apps.cluster.cp.fyre.ibm.com/admin
eem.token=00000000-0000-0000-0000-000000000000
eem.truststore=eem-ca.pem
line.separator=\n.....................................\n
```

## Troubleshooting

To enable debug logging for the formatter, add this to your `tools-log4j.properties` config file:

```
log4j.logger.com.ibm.eventautomation.kafka.formatters=DEBUG
```

## Tips

### Use an alias

Typing the `--formatter` and `--formatter-config` option every time you use `kafka-console-consumer.sh` is time-consuming, so adding an alias to your profile can be a useful time-saver.

For example, if you do something like this:

```sh
alias avro-consumer="CLASSPATH=/absolute/location/of/avro-formatters-0.1.0.jar \
/absolute/location/of/kafka/bin/kafka-console-consumer.sh \
--formatter com.ibm.eventautomation.kafka.formatters.ApicurioFormatter \
--formatter-config /absolute-location/of/apicurio-formatter.properties"
```

This will let you run commands like:

```sh
avro-consumer --bootstrap-server kafka:9092 --topic TOPIC
```

This is particularly useful where `apicurio-formatter.properties` can contain the URL and credentials for a schema registry that you frequently use, giving you a quick and simple way to consume from any of your Avro topics.

### Combine with jq

The formatter outputs the Avro-encoded data in a JSON format, so it pairs well with utilities such as [jq](https://jqlang.org/).

For example, to just print out the `id` property of every event:

```sh
avro-consumer --bootstrap-server kafka:9092 --topic TOPIC | jq -r .id
```


<br>

---

Apache Avro and Avro™ are trademarks of The Apache Software Foundation.
