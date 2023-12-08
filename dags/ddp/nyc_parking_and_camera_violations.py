import datetime
from airflow import DAG

from airflow.operators.empty import EmptyOperator
from airflow.providers.http.operators.http import SimpleHttpOperator

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
    return True if response.status_code == 202 and response.text.trim.lower() in "acknowledged" else False


start = EmptyOperator(
    task_id="start",
    dag=dag
)

download_nyc_parking_violations_file = SimpleHttpOperator(
    task_id='download_nyc_parking_violations_file',
    method='POST',
    http_conn_id='ddp_rest_api_conn',
    endpoint='/nyc/parking-camera/violations/download',
    data={
        "url": "https://data.cityofnewyork.us/resource/nc67-uf89.json?$limit=1000",
        "downloadDir": "/app/data/landing/nyc_violations/",
        "fileName": "nyc1k.json",
        "saveMode": "backup"    # any value other than backup will override existing file if any. backup is timestamped
    },
    headers={"Content-Type": "application/json"},
    do_xcom_push=False,
    response_check=lambda response: handle_response(response),
    dag=dag
)

start >> download_nyc_parking_violations_file