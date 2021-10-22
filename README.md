# student-mobility-broker
[![Build Status](https://github.com/SURFnet/student-mobility-broker/actions/workflows/maven.yml/badge.svg)](https://github.com/SURFnet/student-mobility-broker/actions/workflows/maven.yml/badge.svg)
[![codecov](https://codecov.io/gh/SURFnet/student-mobility-broker/branch/master/graph/badge.svg)](https://codecov.io/gh/SURFnet/student-mobility-broker)

Broker for educational cross-institution registrations.

## [Getting started](#getting-started)

### [System Requirements](#system-requirements)

- Java 8
- Maven 3
- Yarn 1.x
- NodeJS 12+

## [Building and running](#building-and-running)

### [The student-mobility-broker-server](#student-mobility-broker-server)

This project uses Spring Boot and Maven. To run locally, type:

```
cd student-mobility-broker-server
mvn spring-boot:run
```

To build and deploy (the latter requires credentials in your maven settings):

`mvn clean deploy`

### [The student-mobility-broker-client](#student-mobility-broker-client)

The client is build with Svetle and to get initially started:

```
cd student-mobility-broker-client
yarn install
yarn dev
```

Browse to the [application homepage](http://localhost:3003/).
