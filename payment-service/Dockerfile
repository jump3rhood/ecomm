#Stage 1: Build the application
FROM maven:3.9-eclipse-temurin-17 AS builder

#set working directory
WORKDIR /app
# layer caching
COPY pom.xml .

#DOWNLOAD deps
RUN mvn dependency:go-offline -B

#COPY source code
COPY src ./src

# BUILD THE app
RUN mvn clean package -DskipTests -B

#Stage 2: Create the runtime image
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=builder /app/target/payment-service-0.0.1-SNAPSHOT.jar app.jar

#expose the port
EXPOSE 8082

CMD ["java", "-jar", "app.jar"]