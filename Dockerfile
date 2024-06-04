# Base image
FROM openjdk:17-alpine

# Build arguments
ARG JAR_FILE=build/libs/*.jar
ARG PROFILES
ARG ENV

# Copy JAR file into the container
COPY ${JAR_FILE} app.jar

# Set the entry point
ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILES}", "-Dserver.env=${ENV}", "-jar", "app.jar"]