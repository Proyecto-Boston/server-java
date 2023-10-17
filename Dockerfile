FROM maven:3.8.4-openjdk-17

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn package

EXPOSE 1802

CMD ["java", "-jar", "out/artifacts//BostonServer/server-java.jar"]
