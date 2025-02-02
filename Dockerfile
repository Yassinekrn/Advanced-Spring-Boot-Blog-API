FROM openjdk:22
WORKDIR /app
COPY target/springboot-blog-rest-api-0.0.1-SNAPSHOT.jar app.jar
COPY .env /app/.env
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]