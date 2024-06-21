# Stage 1: Build the application using Maven
FROM maven:3.8.6-amazoncorretto-17 as build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run the application using OpenJDK
FROM openjdk:17-ea-10-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar ./app.jar
ENTRYPOINT java -jar app.jar
