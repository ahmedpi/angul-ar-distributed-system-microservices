# Use a lightweight JDK base image
FROM eclipse-temurin:21-jdk-jammy

# Set the working directory
WORKDIR /app

# Copy the built jar file (replace with your actual jar name)
COPY target/*.jar app.jar

# Expose the port (should match the internal port in docker-compose)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]