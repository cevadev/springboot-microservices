# descargamos jre (version minima del java) y manejamos la extraccion
FROM adoptopenjdk:16_36-jre-hotspot as builder
WORKDIR extracted
ADD ./build/libs/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

# construimos la imagen docker que se va a usar en tiempo de ejecucion
FROM adoptopenjdk:16_36-jre-hotspot
WORKDIR application
# indicamos las carpetas que contiene todo fat jar de spring
COPY --from=builder extracted/dependencies/ ./
COPY --from=builder extracted/spring-boot-loader/ ./
COPY --from=builder extracted/snapshot-dependencies/ ./
COPY --from=builder extracted/application/ ./

EXPOSE 8080
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]