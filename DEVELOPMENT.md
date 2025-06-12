# Avro Console formatters - development info

## Dependencies

Unit tests rely on Kafka and Apicurio Registry running in [testcontainers](https://testcontainers.com/) so a Docker engine is required to run tests.

## Build

```sh
mvn package -DskipTests
```

## Run unit tests

```sh
mvn test
```
