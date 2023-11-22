FROM openjdk:11-jdk
LABEL maintainer="yubin"
ARG JAR_FILE=build/libs/*.jar
ADD ${JAR_FILE} app.jar
EXPOSE 8099
ENTRYPOINT ["java","-jar","/app.jar"]
