# ── Stage 1: Build with Maven ──────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml first (layer caching — faster rebuilds)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build the JAR
COPY src ./src
RUN mvn clean package -DskipTests

# ── Stage 2: Lightweight Runtime Image ─────────────────────────────
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy only the built JAR from Stage 1
COPY --from=build /app/target/mapper-0.0.1-SNAPSHOT.jar app.jar

# Expose the port (Render injects $PORT at runtime)
EXPOSE 8080

# Start the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
