# dockerized-data-pipeline

Build the custom image by running `docker build -f ./dockerfiles/DDP-apache-airflow-all -t ddp-airflow-all:v1 .`
and then run the compose file by running
`docker-compose -f docker-compose-all.yml --env-file dev.env up`.

or spin up the simple one by running `docker build -f ./dockerfiles/DDP-apache-airflow -t ddp-airflow:v1 .`
and then run the compose file by running
`docker-compose --env-file dev.env up`.
<h2>Work in progress ...</h2>



