# OpenJDK 17을 기반으로 하는 Docker 이미지 사용
FROM --platform=linux/amd64 openjdk:17-alpine
VOLUME /tmp
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]