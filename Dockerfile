# Use a lightweight Java 17 runtime base image
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy the jar file into the container
COPY target/strim-backend-1.0.0.jar app.jar

# Set the command to run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
