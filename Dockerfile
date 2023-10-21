FROM maven:3.8.6-openjdk-11

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src

COPY out/artifacts/BostonServer/server-java.jar out/artifacts/BostonServer/

RUN chmod +x out/artifacts/BostonServer/server-java.jar


RUN mvn package -Dmaven.compiler.source=11 -Dmaven.compiler.target=11

EXPOSE 2376

CMD ["java", "-jar", "out/artifacts/BostonServer/server-java.jar"]