FROM maven:3.8.1-openjdk-17-slim AS build
RUN mkdir -p /app
WORKDIR /app
COPY pom.xml /app
COPY src /app/src
RUN mvn -B -f pom.xml clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /app/target/*.jar app.jar
EXPOSE 9934

# Define a default value for the Spring profile, but allow overriding via environment variable
ENV SPRING_PROFILES_ACTIVE=prod

ENTRYPOINT ["java","-jar","-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}","app.jar"]
