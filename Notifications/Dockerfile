FROM maven:3.9.6-eclipse-temurin-21 as builder

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim

COPY target/Notifications-0.0.1-SNAPSHOT.jar /app.jar

EXPOSE 18083

ENTRYPOINT ["java", "-jar", "/app.jar"]