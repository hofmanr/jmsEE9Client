# Java JMS Client

This JMS Client must be deployed on an application server. When it's run, it automatically detects the configured JMS queues on the application server.

The client also contains a frontend. With the frontend you can browse the queues and peak into queues to see the messages that are waiting to be consumed.
It is also possible to delete messages fron queues and to put messages on queues.

## Overview
This is a JMS client library for standard JMS implementations. The application is tested with WildFly and works with [ActiveMQ](https://activemq.apache.org/),
the JMS implementation in WildFly. It should also be working on WebLogic and Liberty.

The application consists of the following two parts:
1. Java backend
2. React frontend

The Java part can be found under `src/main/java`. It exposes a REST API that is used by the frontend.
The CORS filter is only meant for local use.
That is when you start the frontend direct from the IDE with `npm start` and run the backend separately on an application server.

The React frontend can can be found under `src/main/ts`. The 'ts' stands for TypeScript, the language that is used to develop the frontend.
You can run the frontend locally with:

    $ npm start

To build the frontend for production:

    $ npm build

It is also possible to build the frontend with Maven, using the profile `prod`:

    $ mvn compile -Pprod

## Installation

### Building from Source
The application can be packaged with:

    ./mvn clean package -Pprod

The profile `prod` ensures that the frontend is build. It uses Node en NPM, which are locally installed.

The artifact that is build is a war file. It can be found in the target directory with the name `jmsRestClient.war`.

### Application Server
When you use WildFly, you can upload the war in WilfFly and start the application.

The frontend can be accessed by the following URL:

    http://localhost:8080/jmsRestClient
