# OMAR AVRO Metadata

## Purpose

The OMAR Avro-Metadata service is responsible to receiving, storing, and retrieving image metadata in JSON format.

## Installation in Openshift

**Assumption:** The omar-avro-metadata docker image is pushed into the OpenShift server's internal docker registry and available to the project.

### Environment variables

|Variable|Value|
|------|------|
|SPRING_PROFILES_ACTIVE|Comma separated profile tags (*e.g. production, dev*)|
|SPRING_CLOUD_CONFIG_LABEL|The Git branch from which to pull config files (*e.g. master*)|
