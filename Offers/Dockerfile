FROM maven:3.9.6-amazoncorretto-21 as builder

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=builder /app/target/Offers-0.0.1-SNAPSHOT.jar /app.jar

EXPOSE 18081

ENTRYPOINT ["java", "-jar", "/app.jar"]
