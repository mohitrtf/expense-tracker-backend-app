FROM openjdk:21
COPY target/spendly-0.0.1-SNAPSHOT.jar spendly-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "spendly-0.0.1-SNAPSHOT.jar"]