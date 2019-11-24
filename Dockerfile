# FROM gradle:4.10.0-jdk8 AS build
FROM gradle:jdk10 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build

# Start with a base image containing Java runtime
FROM openjdk:10-jre-slim

COPY --from=build /home/gradle/src/build/libs/kotlin-crypto-2.2.0.M4.jar .
RUN bash -c 'touch /kotlin-crypto-2.2.0.M4.jar'
ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-Djava.security.egd=file:/dev/./urandom","-jar","/kotlin-crypto-2.2.0.M4.jar"]
