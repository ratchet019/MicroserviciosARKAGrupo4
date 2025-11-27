# Usar imagen base con Java 17
FROM eclipse-temurin:17-jdk-alpine

# Crear directorio para la app
WORKDIR /app

# Copiar el JAR generado por Maven
COPY target/movementservice-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto de la app
EXPOSE 8084

# Definir el comando para arrancar Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]