FROM maven:3.8.4-jdk-8
COPY src /saturnapplication/src
COPY pom.xml /saturnapplication
WORKDIR saturnapplication
RUN mvn clean package