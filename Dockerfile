FROM openjdk:11
EXPOSE 9000
ADD target/technoidentity-api.jar technoidentity-api.jar
ENTRYPOINT ["java","-jar","/technoidentity-api.jar"]
