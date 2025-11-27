# Etapa 1: compilar
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: imagen final
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/mssales-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto configurado en application.yml
EXPOSE 8083

# Ejecuta el jar
ENTRYPOINT ["java","-jar","/app/app.jar"]
