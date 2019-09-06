FROM openjdk:8-jdk-alpine
EXPOSE 9090
RUN mkdir -p /opt/
ADD build/libs/*.jar /opt/app.jar
ENTRYPOINT ["java", "-jar", "/opt/app.jar"]