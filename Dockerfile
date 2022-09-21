FROM ubuntu:22.04

RUN apt update
RUN apt install -y openjdk-18-jre

WORKDIR /app/ebiznes

COPY ./app/build/libs/fat.jar ./

EXPOSE 8080

CMD ["java", "-jar", "fat.jar"]