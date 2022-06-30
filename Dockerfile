FROM openjdk:11
VOLUME /tmp
COPY target/*.jar critics-system.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/urandom", "-jar", "/critics-system.jar"]