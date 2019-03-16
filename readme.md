# Messaging App

This application allows users to post messages and view messages from all users.

Multiple instances of this app can be deployed simultaneously.

## Deployment Diagram

![Uml diagram](docs/deployment.png)

## Getting Started

### Standalone Local Deployment

Note, you will need a local instance of Redis running on port 6379.

Build:

> mvn clean install

Deploy:

Go to the messaging module and run:

> mvn spring-boot:run

To start multiple instances on different ports run:

> mvn -Dserver.port=8081 spring-boot:run

Demo page:

> [http://localhost:8080/](http://localhost:8080/)

Swagger:

> [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)




