FROM gradle:6-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle shadowJar --no-daemon --stacktrace

FROM openjdk:11-jre-slim
EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/app.jar
CMD ["java", "-jar", "/app/app.jar"]