FROM openjdk:11-jre-slim
COPY **/messaging-1.0-SNAPSHOT.jar /usr/src/messaging/app.jar
WORKDIR /usr/src/messaging
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]