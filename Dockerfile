# OpenJDK 17을 기반으로 하는 Docker 이미지 사용
FROM adoptopenjdk/openjdk17:jre-17.0.1_12-alpine

# Docker 컨테이너 내부의 /tmp 디렉토리에 볼륨 마운트
VOLUME /tmp

# 빌드된 JAR 파일을 Docker 이미지 내부로 복사
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Docker 컨테이너 실행 시 실행될 명령어 지정
ENTRYPOINT ["java","-jar","/app.jar"]