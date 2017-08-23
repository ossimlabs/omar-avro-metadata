# OMAR AVRO Metadata

## Dockerfile

Here is an example Dockerfile.  omar-base is no more than a RHEL 7 image with the latest Java installed

```
FROM omar-base
MAINTAINER DigitalGlobe-RadiantBlue
expose 8080
RUN useradd -u 1001 -r -g 0 -d $HOME -s /sbin/nologin -c 'Default Application User' omar
RUN mkdir /usr/share/omar
COPY omar-avro-app-1.0.0-SNAPSHOT.jar /usr/share/omar
RUN chown -R 1001:0 /usr/share/omar
RUN chown 1001:0 /usr/share/omar
RUN chmod -R g+rw /usr/share/omar
RUN find $HOME -type d -exec chmod g+x {} +
USER 1001
WORKDIR /usr/share/omar
CMD ["java", "-server", "-Xms256m", "-Xmx1024m", "-Djava.awt.headless=true", "-XX:+CMSClassUnloadingEnabled", "-XX:+UseGCOverheadLimit", "-Djava.security.egd=file:/dev/./urandom", "-jar", "omar-avro-metadata-app-1.0.0-SNAPSHOT.jar"]
```

Ref: [omar--base](../../../omar-base/docs/install-guide/omar-base/)

## JAR
[https://artifactory.ossim.io/artifactory/webapp/#/artifacts/browse/tree/General/omar-local/omar-avro-metadata](https://artifactory.ossim.io/artifactory/webapp/#/artifacts/browse/tree/General/omar-local/omar-avro-metadata)

##Configuration

```
cloud.aws.credentials.instanceProfile=true
cloud.aws.region.auto=true
cloud.aws.stack.auto=false
server.contextPath=/omar-avro-metadata

```
* **cloud.aws.credentials.instanceProfile** If true, authenticates AWS credentials using the EC2 machine's instance profile instead of AWS keys.
* **cloud.aws.region.auto** If true, automatically detects the AWS region based on the EC2 machine instance profile.
* **cloud.aws.stack.auto** If true, automatically configures the AWS cloud stack using CloudFormation. This is set to false, as we do not use CloudFormation.
* **server.contextPath** Sets the context path of the server from /
