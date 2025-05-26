# Base image
FROM eclipse-temurin:17-jdk-alpine

# 앱 디렉터리 생성
WORKDIR /app

# 빌드된 JAR 복사
COPY build/libs/*.jar app.jar

# 컨테이너 시작 시 실행할 커맨드
ENTRYPOINT ["java", "-jar", "/app/app.jar"]