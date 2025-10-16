# Etapa 1: Build con Gradle
FROM gradle:8.5-jdk21 AS builder

WORKDIR /app

# Copiar archivos de configuración
COPY build.gradle .
COPY settings.gradle .
COPY gradle.properties .
COPY gradle/ gradle/
COPY gradlew .
COPY gradlew.bat .

# Copiar código fuente
COPY src/ src/

# Compilar el proyecto
RUN chmod +x gradlew && \
    ./gradlew build -x test --no-daemon

# Etapa 2: Runtime con Java 21 ligero
FROM openjdk:21-slim

WORKDIR /app

# Copiar JAR desde etapa de build
COPY --from=builder /app/build/libs/control-financiero-backend-1.0.0.jar app.jar

# Exponer puerto (Render asignará uno dinámicamente)
EXPOSE 8080

# Variables de entorno
ENV JAVA_OPTS="-Dserver.port=8080"

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]