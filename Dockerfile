# Etapa 1: Construcción (Build)
# Usamos una imagen con JDK 25 para compilar el proyecto
FROM eclipse-temurin:25-jdk-alpine AS build
WORKDIR /app

# Copiamos el código fuente y los archivos de Maven
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
# Damos permisos de ejecución al wrapper de Maven
RUN chmod +x mvnw
# Descargamos las dependencias (para aprovechar el caché de Docker)
RUN ./mvnw dependency:go-offline

COPY src ./src
# Compilamos el proyecto omitiendo los tests para ganar velocidad
RUN ./mvnw clean package -DskipTests

# Etapa 2: Ejecución (Runtime)
# Usamos una imagen más ligera solo con el JRE 25
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

# Copiamos el archivo .jar generado en la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto que usa Spring Boot
EXPOSE 8080

# Comando para arrancar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]