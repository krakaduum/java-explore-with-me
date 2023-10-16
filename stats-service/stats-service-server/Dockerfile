FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar stats-service-server.jar
ENTRYPOINT ["java","-jar","/stats-service-server.jar"]