import datetime
import json

from airflow import DAG

from airflow.operators.bash import BashOperator
from airflow.operators.empty import EmptyOperator
from airflow.providers.http.operators.http import SimpleHttpOperator

from airflow.sensors.filesystem import FileSensor

default_args = {
    "owner": "Hamid",
    "retries": 1,
    "retry_delay": datetime.timedelta(minutes=5)
}

dag = DAG(
    dag_id="ddp.nyc_parking_and_camera_violations",
    start_date=datetime.datetime.now() - datetime.timedelta(days=1),
    schedule_interval=None,
    catchup=False,
    max_active_runs=1,
    max_active_tasks=2,
    default_args=default_args,
    tags=["ddp"]
)


def handle_response(response):
    print("Response: " + str(response))
    print("Body: " + response.text)
    return True if response.status_code == 202 and response.text.strip().lower() in "acknowledged" else False


start = EmptyOperator(
    task_id="start",
    dag=dag
)

download_nyc_parking_violations_file = SimpleHttpOperator(
    task_id='download_nyc_parking_violations_file',
    method='POST',
    http_conn_id='ddp_rest_api_conn',
    endpoint='/nyc/parking-camera/violations/download',
    data=json.dumps({
        "url": "https://data.cityofnewyork.us/resource/nc67-uf89.json?$limit=1000",
        "downloadDir": "/app/data/landing/nyc_violations/",
        "fileName": "nyc1k.json",
        "mode": "backup",    # any value other than backup will override existing file if any
        "backupDir": "/app/data/archive/nyc_violations/"
    }),
    headers={"Content-Type": "application/json"},
    do_xcom_push=False,
    response_check=lambda response: handle_response(response),
    dag=dag
)

check_for_success_file = FileSensor(
    task_id="check_for_success_file",
    fs_conn_id="fs_local_conn",
    filepath="/app/data/landing/nyc_violations/_success",
    poke_interval=5,
    timeout=60 * 5,
    mode='reschedule'
    # mode='poke'
)

process_nyc_camera_parking_violations_file = BashOperator(
    task_id='process_nyc_camera_parking_violations_file',
    bash_command="/app/scripts/ddp/process_nyc_violations_file.sh ",
    do_xcom_push=False,
    dag=dag,
    append_env=True,
    env={
        "filePath": "/app/data/landing/nyc_violations/nyc1k.json",
        "processedDir": "/app/data/processed/nyc_violations/",
        "archivedDir": "/app/data/archive/nyc_violations/"
    }
)

start >> download_nyc_parking_violations_file >> check_for_success_file >> process_nyc_camera_parking_violations_file

