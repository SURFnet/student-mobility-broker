# student-mobility-broker
[![Build Status](https://github.com/SURFnet/student-mobility-broker/actions/workflows/maven.yml/badge.svg)](https://github.com/SURFnet/student-mobility-broker/actions/workflows/maven.yml/badge.svg)
[![codecov](https://codecov.io/gh/SURFnet/student-mobility-broker/branch/master/graph/badge.svg)](https://codecov.io/gh/SURFnet/student-mobility-broker)

Broker for educational cross-institution registrations.

## [Getting started](#getting-started)

### [System Requirements](#system-requirements)

- Java 8
- Maven 3
- Yarn 1.x
- NodeJS 14+

Set the JAVA_HOME property for maven (example for macOS):
```
export JAVA_HOME=/Library/Java/JavaVirtualMachines/adoptopenjdk-8.jdk/Contents/Home/
```

The use of a MySQL database is optional. If you want to enable sessions backed up by the database, create a database:

```sql
DROP DATABASE IF EXISTS student_mobility;
CREATE DATABASE student_mobility CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
CREATE USER 'student_mobility'@'localhost' IDENTIFIED BY 'secret';
GRANT ALL privileges ON `student_mobility`.* TO 'student_mobility'@'localhost';
```


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

The client is build with Svelte and to get initially started:

```
cd student-mobility-broker-client
yarn install
yarn dev
```

Browse to the [application homepage](http://localhost:3003/).
