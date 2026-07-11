# --- Stage 1: Build the app with Maven ---
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# --- Stage 2: Run it with just a lightweight Java runtime ---
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/store-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
