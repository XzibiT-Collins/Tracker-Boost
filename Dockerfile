#Building Project Tracker
FROM maven:3.9.9-eclipse-temurin-24-alpine AS builder

WORKDIR /app

#copy pom.xml file to root directory
COPY pom.xml .
#download dependencies
RUN mvn dependency:go-offline

#Copy remaining source code into src folder
COPY src ./src

#Package the application but skip Tests
RUN mvn clean package -DskipTests

#Build layer 2. Set up JDK environment
FROM eclipse-temurin:24-jdk-alpine

WORKDIR /app

#Copy the java compiled file .jar from builder stage
COPY --from=builder /app/target/*.jar app.jar

#Expose port for application
EXPOSE 8080

#Run springboot application
ENTRYPOINT ["java", "-jar", "app.jar"]