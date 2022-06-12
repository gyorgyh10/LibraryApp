FROM adoptopenjdk:11-jre-hotspot
RUN mkdir /opt/app
COPY target/vizsgaremek-gyorgyh10-1.0-SNAPSHOT.jar /opt/app/vizsgaremek-gyorgyh10.jar
CMD ["java", "-jar", "/opt/app/vizsgaremek-gyorgyh10.jar"]