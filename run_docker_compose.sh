#!/bin/bash

### Path to local file system to be used in volumes
export MOUNT_VOL=/Users/hamidurrahman/Downloads/GitHub/dockerized-data-pipeline

### airflow-postgres meta db exports
export AIRFLOW_META_DB_USERNAME=airflow
export AIRFLOW_META_DB_PASS=pass
export AIRFLOW_META_DB=airflow


### airflow-webserver exports
export AIRFLOW_WEBSERVER_USERNAME=admin
export AIRFLOW_WEBSERVER_PASS=admin


### run the docker compose to start up all services
docker-compose up
# docker-compose up -d
