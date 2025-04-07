FROM gradle:8.5-jdk17 as builder

WORKDIR /app

COPY . .

RUN gradle assemble

FROM eclipse-temurin:17-jre-focal

WORKDIR /app

COPY --from=builder /app/build/libs/cardgame-challenge.jar /app/cardgame-challenge.jar

ENTRYPOINT ["java", "-jar", "cardgame-challenge.jar"]
