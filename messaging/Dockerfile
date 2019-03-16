FROM openjdk:11-jre-alpine
COPY ./target/messaging.jar /usr/src/messaging/
WORKDIR /usr/src/messaging
EXPOSE 8080
CMD ["java", "-jar", "messaging.jar"]