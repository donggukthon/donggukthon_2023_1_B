# Dockerfile

# jdk17 Image Start
FROM openjdk:17

# 인자 설정 - JAR_File
ARG JAR_FILE=build/libs/*.jar

# jar 파일과 application.yml 파일 복제
COPY ${JAR_FILE} app.jar
COPY src/main/resources/application.yml application.yml

# 실행 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]