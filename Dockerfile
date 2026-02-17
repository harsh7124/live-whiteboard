FROM maven:3.8.7-openjdk-11 AS build
WORKDIR /workspace
COPY pom.xml ./
COPY src ./src
RUN mvn -f pom.xml -DskipTests package

FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=build /workspace/target/live-whiteboard-0.1.0.jar ./app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
