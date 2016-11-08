FROM java:8
ENV APP_DIR /srv
WORKDIR $APP_DIR
CMD ["./gradlew", "clean", "build"]
ADD ./build/libs/demo-0.0.1-SNAPSHOT.jar $APP_DIR/demo.jar
CMD java -Djava.security.egd=file:/dev/./urandom -jar demo.jar
