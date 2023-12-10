#!/usr/bin/env bash

airflow db init;

airflow variables import /app/scripts/airflow/variables.json;

airflow connections add 'fs_local_conn' --conn-type 'fs' --conn-extra '{"path": "/"}';

airflow connections add 'ddp_rest_api_conn' --conn-type 'http' --conn-host 'ddp-rest-api' --conn-port '7000';

airflow users create --role ${AF_ROLE} --username ${AF_USERNAME} --password ${AF_PASS} \
  --firstname ${AF_FIRSTNAME} --lastname ${AF_LASTNAME} --email ${AF_EMAIL};

airflow webserver >> ${AIRFLOW_HOME}/logs/airflow-webserver.log 2>&1

