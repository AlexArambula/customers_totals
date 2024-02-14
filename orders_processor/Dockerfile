FROM maven:3-openjdk-17-slim AS bld
ARG skipTests=true

RUN mkdir -p /mvn/project
COPY pom.xml /mvn/project
COPY src /mvn/project/src/

WORKDIR /mvn/project
RUN mvn clean package -Dmaven.test.skip=$skipTests

FROM openjdk:17-jdk-slim

COPY --from=bld /mvn/project/target/orders_processor*.jar /home/app.jar
WORKDIR /home
ENTRYPOINT ["sh", "-c", "java -jar app.jar"]