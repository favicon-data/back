FROM amazoncorretto:17.0.12
COPY build/libs/app.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
