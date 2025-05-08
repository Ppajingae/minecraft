FROM ubuntu:24.04
RUN apt-get update && \
    apt-get install -y openjdk-21-jdk libcurl4 && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY *.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
