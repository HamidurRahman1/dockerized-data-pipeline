# dockerized-data-pipeline

* <b>Steps to run:</b>

  1. Update `MOUNT_VOL` in `dev.env` file if you want to store all data generated by the containers in a specific folder. 
  It will be relative to the project/repo dir if left alone as it is.

  2. `cd` into project/repo directory.

  3. Build one of the custom docker images (image name and tag is used in `ddp-airflow-compose.yml`) -
     1. Run the multi-stage dockerfile (image size: ~1.73 GB) - 
     `DOCKER_BUILDKIT=1 docker build -f ./dockerfiles/ddp-airflow-multi-stage -t ddp-airflow:v1 . --target=RUNTIME`
     2. Run the single stage dockerfile (image size: ~2.28 GB) - 
     `docker build -f ./dockerfiles/ddp-airflow -t ddp-airflow:v1 .`

  4. Spin up the init compose file and wait until vault server is up and running - 
  `docker-compose -f ./ddp-init-compose.yml --env-file dev.env up`
  
  5. Finally, spin up the airflow compose file - 
  `docker-compose -f ./ddp-airflow-compose.yml --env-file dev.env --env-file ./vault/vol/keys/vault-token.env up`


* Vault UI: http://localhost:8200/
  * Vault token is available in: `vault/vol/keys/vault-token.env`


* PHP LDAP Admin: http://localhost:8001/
  * Login DN: `cn=admin,dc=ddp,dc=com`
  * password: `admin`
* Airflow Webserver: http://localhost:8000/login/ 
  * users:
    * username: `hrahman`, password: `hrahman1` (Admin Role)
    * username: `jdoe`, password: `jdoe1` (Viewer Role)
  * You may trigger both `ddp.failed_banks_processor` and `ddp.nyc_parking_and_camera_violations` and head over to flower UI to see if scheduler has distributed the work to both worker nodes or not.
  * Invoke the test REST API DAG - 
    * `curl -X 'POST' 'http://localhost:8000/api/v1/dags/basic.called_via_rest_api/dagRuns' -u "hrahman:hrahman1" -H 'accept: application/json' -H 'Content-Type: application/json' -d '{ "conf": { "param1": "value 1", "param2": "value 2" } }'`
* Celery flower UI: http://localhost:9000/
* DDP rest api: http://localhost:7000/


* <b>Nice to have:</b>
  * <s>Multi-stage docker build.</s> (implemented)
  * <s>Use LDAP for airflow webserver.</s> (implemented)
  * <s>Vault or similar for storing database credentials.</s> (implemented)
  * Logging instead of sout.
  * Use `hdfs` instead of local file system.
