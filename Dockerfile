# Stage 1: Builder
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /build

# Copy pom.xml and download dependencies (for layer caching)
COPY app/pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build
COPY app/src ./src
RUN mvn clean package -Duser.timezone=Asia/Kolkata

# Stage 2: Runtime
FROM alpine/java:22-jre

WORKDIR /app

# Copy the built jar from builder stage
COPY --from=builder /build/target/*.jar bank.jar

# Create non-root user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Expose application port
EXPOSE 8080

# Health check
# HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
#   CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Run the application
CMD ["java", "-jar", "bank.jar"]