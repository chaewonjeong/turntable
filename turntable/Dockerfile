# Use a base image containing Java 17 runtime
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the Gradle wrapper and build files
COPY gradlew /app/
COPY gradle /app/gradle
COPY build.gradle /app/
COPY settings.gradle /app/

# Copy the source code
COPY src /app/src

# Make the Gradle wrapper executable
RUN chmod +x gradlew

# Install Tomcat Jasper for JSP support
RUN apt-get update && apt-get install -y \
    libtomcat9-java \
    && rm -rf /var/lib/apt/lists/*

# Expose port 8090
EXPOSE 8080

# Command to run the application
CMD ["./gradlew", "bootrun"]