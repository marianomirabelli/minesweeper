FROM openjdk:14

ARG VERSION

COPY api-minesweeper-service/target/api-minesweeper-service-${VERSION}.jar /app.jar

ENTRYPOINT ["java","-jar","/app.jar"]