FROM eclipse-temurin:21-jdk
COPY spendly-0.0.2-SNAPSHOT.jar spendly-0.0.2-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "spendly-0.0.2-SNAPSHOT.jar"]