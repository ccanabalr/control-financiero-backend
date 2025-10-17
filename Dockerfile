# Etapa 1: Build con Gradle
FROM gradle:8.5-jdk17 AS builder

WORKDIR /app

# Copiar archivos de configuración de Gradle
COPY build.gradle .

# Copiar código fuente
COPY src/ src/

# Compilar el proyecto usando gradle (ya incluido en la imagen)
RUN gradle build -x test --no-daemon --build-cache

# Etapa 2: Runtime con Java 21 ligero
FROM openjdk:17-slim

WORKDIR /app

# Copiar JAR desde etapa de build
COPY --from=builder /app/build/libs/control-financiero-backend-1.0.0.jar app.jar

# Exponer puerto (Render asignará uno dinámicamente)
EXPOSE 8080

# Comando de inicio
# -Dserver.port=$PORT: Puerto dinámico de Render
# -Dspring.profiles.active=production: Cargar profile production
# -XX:+UseG1GC: Garbage collector optimizado
# -XX:MaxRAMPercentage=75: Usar hasta 75% de RAM disponible
ENTRYPOINT ["java", "-Dserver.port=${PORT:-8080}", "-Dspring.profiles.active=production", "-XX:+UseG1GC", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]