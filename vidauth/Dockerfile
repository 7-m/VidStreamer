FROM openjdk:11
COPY build/libs/vidauth-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar","--spring.profiles.active=prod"]
