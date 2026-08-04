# Stage 1: Build stage using Maven 3.9 and Temurin JDK 21 Alpine
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder

# Set working directory for build stage
WORKDIR /app

# Copy pom.xml first to optimize Docker layer caching for dependencies
COPY pom.xml .

# Download dependencies in offline mode
RUN mvn dependency:go-offline -B

# Copy application source code
COPY src ./src

# Build production executable jar skipping unit tests
RUN mvn clean package -DskipTests

# Stage 2: Production runtime stage using lightweight Temurin JRE 21 Alpine
FROM eclipse-temurin:21-jre-alpine

# Set application working directory
WORKDIR /app

# Copy the built jar from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose TransferFlow service default port
EXPOSE 8083

# Set container entrypoint command
ENTRYPOINT ["java", "-jar", "app.jar"]
