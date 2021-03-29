FROM nexus-docker-public-hosted.ossim.io/openjdk11:alpine-slim
ENV SPRING_CLOUD_CONFIG_ENABLED=true \
    SPRING_CLOUD_DISCOVERY_ENABLED=true \
    HOME=/home/omar
WORKDIR $HOME
EXPOSE 8080
COPY build/libs/*.jar $HOME
CMD $HOME/run.sh
