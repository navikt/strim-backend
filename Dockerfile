FROM gcr.io/distroless/java21-debian12
ENV TZ="Europe/Oslo"
COPY /target/strim-backend-1.0.0.jar app.jar
CMD ["app.jar"]