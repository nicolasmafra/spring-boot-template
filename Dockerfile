FROM openjdk:11-jre-slim

COPY  ./certificados/ ./certificados/
COPY  ./target/*.jar ./app.jar

# Zone
ENV TZ=America/Sao_Paulo
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

ENTRYPOINT ["java", "-jar", "app.jar"]
