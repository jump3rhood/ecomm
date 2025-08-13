#Stage 1: Build the application
FROM maven:3.9-eclipse-temurin-17 AS builder

# set working directory
WORKDIR /app

# COPY pom.xml (layer caching ~Docker)
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline -B

#COPY source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests -B

# Stage 2: Create runtime image
FROM eclipse-temurin:17-jre

WORKDIR /app

#copy the jar from builder stage
COPY --from=builder /app/target/email-service-0.0.1-SNAPSHOT.jar app.jar

#expose the port
EXPOSE 8181

# RUN the application
CMD ["java", "-jar", "app.jar"]