FROM openjdk:17-jdk-alpine
LABEL authors="willy-watcho"
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

ENTRYPOINT ["top", "-b"]