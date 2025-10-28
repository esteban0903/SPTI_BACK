# syntax=docker/dockerfile:1
# Multi-stage Dockerfile for a Java 21 Maven project.
# Builds the application with Maven and runs the resulting jar with a lightweight JRE.

FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy only what is needed for dependency resolution first (speeds up rebuilds)
COPY pom.xml .
# If you have a Maven wrapper and want to use it, copy mvnw and .mvn as well:
# COPY mvnw .mvn/ ./
RUN mvn -B -f pom.xml dependency:go-offline

# Copy the source and build
COPY src ./src
RUN mvn -B -DskipTests package

# Runtime image
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the built jar (wildcard to match typical jar names)
COPY --from=build /app/target/*.jar ./app.jar

# Update if your app listens on a different port
EXPOSE 8080

# Adjust JVM options as needed
# Create a non-root user and ensure app directory is owned by it
RUN addgroup --system app && adduser --system --ingroup app app
RUN chown -R app:app /app
USER app

ENTRYPOINT ["java","-jar","/app/app.jar"]