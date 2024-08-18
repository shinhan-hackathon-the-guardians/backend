FROM --platform=linux/amd64 openjdk:17
LABEL authors="yijy001"

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]