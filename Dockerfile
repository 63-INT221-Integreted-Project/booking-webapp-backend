FROM openjdk:16.0
EXPOSE 8080
ADD ./target/bookingproj-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]