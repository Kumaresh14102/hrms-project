

# Stage 1: Build (The "Worker" stage - deleted after build)
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime (The "Final" stage - using your custom base)
FROM hrms-java-base:17
WORKDIR /app

# Ensure we are root to create the user
USER root

# Create a slim user for security
RUN addgroup -S spring && adduser -S spring -G spring

# Copy only the JAR from the worker stage
COPY --from=build /app/target/*.jar app.jar

USER spring:spring
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]



