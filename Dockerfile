FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/Task_Management_System-0.0.1-SNAPSHOT.jar /app/app.jar
ENTRYPOINT ["java","-jar","app.jar"]