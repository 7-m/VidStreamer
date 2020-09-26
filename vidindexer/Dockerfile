FROM openjdk:11
COPY build/libs/vidindexer-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar","--spring.profiles.active=prod", "-Xmx1G"]
