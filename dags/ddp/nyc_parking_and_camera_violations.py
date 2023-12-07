import datetime
from airflow import DAG

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


def handle(response):
    # print(str(response))
    # print(response.text)
    return True


download_nyc_parking_violations_file = SimpleHttpOperator(
    task_id='download_nyc_parking_violations_file',
    method='GET',
    http_conn_id='ddp_rest_api_conn',
    endpoint='/nyc/data',
    headers={"Content-Type": "application/json"},
    do_xcom_push=False,
    response_check=lambda response: handle(response),
    dag=dag
)


download_nyc_parking_violations_file
