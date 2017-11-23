# OMAR AVRO Metadata

## Source Location
[https://github.com/ossimlabs/omar-avro-metadata](https://github.com/ossimlabs/omar-avro-metadata)

## Purpose
The Avro Metadata application puts data from an Avro file into a database and returns the data upon request.

## Installation in Openshift

**Assumption:** The omar-avro-metadata-app docker image is pushed into the OpenShift server's internal docker registry and available to the project.

### Persistent Volumes
**None**

### Environment variables

|Variable|Value|
|------|------|
|SPRING_PROFILES_ACTIVE|Comma separated profile tags (*e.g. production, dev*)|
|SPRING_CLOUD_CONFIG_LABEL|The Git branch from which to pull config files (*e.g. master*)|

### An Example DeploymentConfig
```yaml
apiVersion: v1
kind: DeploymentConfig
metadata:
  annotations:
    openshift.io/generated-by: OpenShiftNewApp
  creationTimestamp: null
  generation: 1
  labels:
    app: omar-openshift
  name: omar-avro-metadata-app
spec:
  replicas: 1
  selector:
    app: omar-openshift
    deploymentconfig: omar-avro-metadata-app
  strategy:
    resources: {}
    rollingParams:
      intervalSeconds: 1
      maxSurge: 25%
      maxUnavailable: 25%
      timeoutSeconds: 600
      updatePeriodSeconds: 1
    type: Rolling
  template:
    metadata:
      annotations:
        openshift.io/generated-by: OpenShiftNewApp
      creationTimestamp: null
      labels:
        app: omar-openshift
        deploymentconfig: omar-avro-metadata-app
    spec:
      containers:
      - env:
        - name: SPRING_PROFILES_ACTIVE
          value: production,dev
        - name: SPRING_CLOUD_CONFIG_LABEL
          value: master
        image: 172.30.181.173:5000/o2/omar-avro-metadata@sha256:582838755a9261d27fc4e6bdcd361ff05faeb95ee46293f6e7ddfc9ba8d20171
        imagePullPolicy: Always
        livenessProbe:
          failureThreshold: 3
          initialDelaySeconds: 60
          periodSeconds: 10
          successThreshold: 1
          tcpSocket:
            port: 8080
          timeoutSeconds: 1
        name: omar-avro-metadata-app
        ports:
        - containerPort: 8080
          protocol: TCP
        readinessProbe:
          failureThreshold: 3
          initialDelaySeconds: 30
          periodSeconds: 10
          successThreshold: 1
          tcpSocket:
            port: 8080
          timeoutSeconds: 1
        resources: {}
        terminationMessagePath: /dev/termination-log
      dnsPolicy: ClusterFirst
      restartPolicy: Always
      securityContext: {}
      terminationGracePeriodSeconds: 30
  test: false
  triggers:
  - type: ConfigChange
  - imageChangeParams:
      automatic: true
      containerNames:
      - omar-avro-metadata-app
      from:
        kind: ImageStreamTag
        name: omar-avro-metadata:latest
        namespace: o2
    type: ImageChange
```

## Configuration
|Variable|Value|Description|
|------|------|------|
|cloud.aws.credentials.instanceProfile|true|If true, authenticates AWS credentials using the EC2 machine's instance profile instead of AWS keys.|
|cloud.aws.region.auto|true|If true, automatically detects the AWS region based on the EC2 machine instance profile.|
|cloud.aws.stack.auto|false|If true, automatically configures the AWS cloud stack using CloudFormation. This is set to false, as we do not use CloudFormation.|
|server.contextPath|omar-avro-metadata|Sets the context path of the server from /|


## Application Configuration YAML
