FROM maven:3.9.0-eclipse-temurin-19

ADD . /app/

WORKDIR /app

EXPOSE 80

RUN rm -rf target/*; \
    mvn clean package -Dmaven.test.skip; \
    mkdir logs

CMD java -jar /app/target/poc-0.0.1-SNAPSHOT.jar > /app/logs/logs.txt