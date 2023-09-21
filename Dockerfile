FROM openjdk:17
COPY build/libs/application-plain.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]