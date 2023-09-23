FROM openjdk:17

ENV LANG en_US.UTF-8 \
    LANGUAGE en_US:en

COPY build/libs/application-*[^tesourcplain].jar /usr/app/app.jar

#WORKDIR /usr/app
#USER root

ENTRYPOINT ["java", "-jar", "/usr/app/app.jar"]
