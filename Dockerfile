FROM gcr.io/distroless/java21-debian12
ENV LANG='nb_NO.UTF-8' LANGUAGE='nb_NO:nb' LC_ALL='nb:NO.UTF-8' TZ="Europe/Oslo"
# Merk at JDK_JAVA_OPTIONS blir satt via naiserator-*.yml, og derfor ikke bør settes her.
# Dette gjøres for at JAVA_PROXY_OPTIONS-verdien som Nais setter ved oppstart i Kubernetes skal kunne tas med; den vil ikke være satt/ha
# riktig verdi under bygging av Docker-imaget.
WORKDIR /app
COPY build/libs/app*.jar app.jar
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]