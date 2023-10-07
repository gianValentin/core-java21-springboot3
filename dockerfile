FROM openjdk:21-jdk-oracle
COPY target/core-java21-0.0.1-SNAPSHOT.jar java-app.jar
ENTRYPOINT [ "java", "-jar", "java-app.jar"]