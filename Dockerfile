FROM azul/zulu-openjdk:11
EXPOSE 8080
RUN groupadd spring && useradd -g spring spring
USER spring:spring
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app.jar"]
