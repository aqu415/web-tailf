FROM openjdk:8-jre
COPY /app/tailf-web-1.2.1-SNAPSHOT.jar /app/tailf-web-1.2.1-SNAPSHOT.jar
COPY /app/classes/application.yaml /app/log/
WORKDIR /app
CMD ["java", "-jar", "/app/tailf-web-1.2.1-SNAPSHOT.jar"]
