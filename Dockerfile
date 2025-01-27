FROM gradle:8.12-jdk21 AS build

WORKDIR /app

COPY build.gradle settings.gradle gradlew /app/
COPY gradle /app/gradle/
COPY src /app/src/

RUN gradle build --no-daemon

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/build/libs/book-tracker-service-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
