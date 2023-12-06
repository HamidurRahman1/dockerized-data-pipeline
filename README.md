# dockerized-data-pipeline

* Steps to run:

1. `cd` into project/repo directory.

2. Build the custom image
`docker build -f ./dockerfiles/ddp-airflow -t ddp-airflow:v1 . --build-arg DB_URL=jdbc:postgresql://ddp-postgres-metadb:5432/ddp_db --build-arg DB_USER=ddp_user --build-arg DB_PASS=ddp_pass`

3. Spin up the compose file
`docker-compose -f ./ddp-airflow-compose.yml --env-file dev.env up` 

4. Finally, 
exec into <b>ddp-airflow-webserver</b> container and run 
`airflow connections add 'ddp_rest_api_conn' --conn-type 'http' --conn-host 'ddp-rest-api' --conn-port '7000'`


* Airflow Webserver: http://localhost:8000/login/ (admin:admin)
* Celery flower UI: http://localhost:9000/
* DDP-rest-api: http://localhost:7000/


