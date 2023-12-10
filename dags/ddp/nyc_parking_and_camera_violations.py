import os
import json
import datetime

from airflow import DAG
from airflow.models import Variable

from airflow.operators.bash import BashOperator
from airflow.operators.empty import EmptyOperator
from airflow.providers.http.operators.http import SimpleHttpOperator

from airflow.sensors.filesystem import FileSensor

default_args = {
    "owner": "Hamid",
    "retries": 1,
    "retry_delay": datetime.timedelta(minutes=5)
}

filename = "nyc_violations.json"

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
        "url": Variable.get("NYC_VIOLATIONS_URL"),
        "downloadDir": Variable.get("NYC_VIOLATIONS_DOWNLOAD_DIR"),
        "fileName": filename
    }),
    headers={"Content-Type": "application/json"},
    do_xcom_push=False,
    response_check=lambda response: handle_response(response),
    dag=dag
)

check_for_success_file = FileSensor(
    task_id="check_for_success_file",
    fs_conn_id="fs_local_conn",
    filepath=Variable.get("NYC_VIOLATIONS_SUCCESS_FILE"),
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
        "filePath": os.path.join(Variable.get("NYC_VIOLATIONS_DOWNLOAD_DIR"), filename),
        "processedDir": Variable.get("NYC_VIOLATIONS_PROCESSED_DIR"),
        "archivedDir": Variable.get("NYC_VIOLATIONS_ARCHIVE_DIR")
    }
)

start >> download_nyc_parking_violations_file >> check_for_success_file >> process_nyc_camera_parking_violations_file

