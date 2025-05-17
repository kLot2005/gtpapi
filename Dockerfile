FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/*.jar gtpapi-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/gtpapi-0.0.1-SNAPSHOT.jar"]
