FROM openjdk:14

ARG VERSION

COPY api-minesweeper-service/target/api-minesweeper-service-${VERSION}.jar /app.jar

EXPOSE 8080/tcp

CMD ["java","-jar","/app.jar"]