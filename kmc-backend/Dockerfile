FROM maven:3.5-jdk-8-alpine AS build
LABEL author="Jonathan Langens <jonathan.langens@tenforce.com>"

ENV KMCBACKENDVERSION=0.1
WORKDIR /app
ADD . /app
RUN mvn clean install

FROM tomcat:9-jre8-alpine
WORKDIR /usr/local/tomcat/webapps
RUN rm -rf ./ROOT/ 
ENV KMCBACKENDVERSION=0.1
COPY --from=build /app/target/KMC-BACKEND-${KMCBACKENDVERSION}.war ./ROOT.war
