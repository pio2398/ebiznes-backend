FROM ubuntu:22.04


RUN apt update
RUN apt install -y openjdk-18-jre
RUN ls -a

WORKDIR /app/ebiznes

COPY ./build/libs/ebiznes-all.jar ./

EXPOSE 8080

CMD ["java", "-jar", "ebiznes-all.jar"]


