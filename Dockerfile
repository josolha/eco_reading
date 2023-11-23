#FROM openjdk:11-jdk
#ADD /build/libs/*.jar app.jar
#ENTRYPOINT ["java","-Dspring.profiles.active=main","-jar","/app.jar"]

FROM adoptopenjdk/openjdk11
ARG JAR_FILE_PATH=build/libs/*.jar
COPY ${JAR_FILE_PATH} app.jar
EXPOSE 8099
ENTRYPOINT ["java", "-jar", "app.jar"]
