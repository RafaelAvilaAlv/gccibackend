# Etapa de build
FROM maven:3.9.9-eclipse-temurin-17 AS builder
WORKDIR /app

# Copiar primero archivos de Maven para aprovechar caché
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Dar permisos al wrapper
RUN chmod +x mvnw

# Descargar dependencias
RUN ./mvnw -q -DskipTests dependency:go-offline

# Copiar código fuente
COPY src src

# Compilar jar
RUN ./mvnw clean package -DskipTests

# Etapa de runtime
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copiar jar generado
COPY --from=builder /app/target/*.jar app.jar

# Render inyecta PORT; Spring lo toma con server.port=${PORT:8080}
EXPOSE 10000

ENTRYPOINT ["java","-jar","/app/app.jar"]