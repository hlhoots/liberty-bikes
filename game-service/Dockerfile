FROM open-liberty:microProfile1
ADD --chown=1001:0 build/libs/game-service.war /config/dropins
COPY --chown=1001:0 src/main/liberty/config /config/
RUN printf 'httpPort=8080\n\
httpsPort=8443' > /config/bootstrap.properties
#RUN printf -- "-Dorg.libertybikes.restclient.PlayerService/mp-rest/url=\
#http://lb-player:8081/" > /config/jvm.options


EXPOSE 8080 8443