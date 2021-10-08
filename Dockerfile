FROM openjdk:11
VOLUME /tmp
COPY build/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]