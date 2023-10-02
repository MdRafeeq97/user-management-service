FROM openjdk:8-jdk-alpine
COPY target/user-service.jar user-service.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=dev", "user-service.jar"]