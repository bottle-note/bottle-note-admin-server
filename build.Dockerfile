FROM eclipse-temurin:17-jdk AS builder
WORKDIR /app
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src
RUN ./gradlew build -x test

FROM eclipse-temurin:17-jre
WORKDIR /app
ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
RUN mkdir -p config
COPY --from=builder /app/build/libs/*.jar /app.jar
ENV SPRING_PROFILES_ACTIVE=default
ENTRYPOINT ["java", "-jar", "/app.jar"]
