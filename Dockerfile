FROM ubuntu:22.04

RUN apt-get update && \
    apt-get install -y openjdk-17-jdk && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
ENV PATH=$JAVA_HOME/bin:$PATH

WORKDIR /app

COPY src ./src

EXPOSE 1802

RUN mkdir -p bin && \
    javac -d bin -cp src/main/java src/main/java/SOAP/classes/File.java && \
    javac -d bin -cp src/main/java src/main/java/SOAP/classes/Folder.java && \
    javac -d bin -cp src/main/java src/main/java/SOAP/classes/URLS.java && \
    javac -d bin -cp src/main/java src/main/java/SOAP/classes/Response.java && \
    javac -d bin -cp src/main/java src/main/java/SOAP/classes/User.java && \
    javac -d bin -cp src/main/java src/main/java/SOAP/classes/Service.java && \
    javac -d bin -cp src/main/java src/main/java/Server.java

CMD ["java", "-cp", "bin", "Server"]
