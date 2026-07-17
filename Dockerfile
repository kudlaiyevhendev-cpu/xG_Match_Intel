# syntax=docker/dockerfile:1

# --- Build stage: JDK + Maven ---
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build
# Cache dependencies first
COPY pom.xml .
RUN mvn -B -q dependency:go-offline
# Then build
COPY src ./src
RUN mvn -B -q clean package -DskipTests

# --- Runtime stage: slim JRE ---
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /build/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
