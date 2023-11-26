import datetime
import json
import requests
import shutil
from airflow import DAG

from airflow.operators.python import PythonOperator
from airflow.providers.http.operators.http import SimpleHttpOperator

default_args = {
    "owner": "Hamid",
    "retries": 1,
    "retry_delay": datetime.timedelta(minutes=5)
}

dag = DAG(
    dag_id="failed_banks_processor",
    start_date=datetime.datetime.now(),
    schedule_interval=None,
    catchup=False,
    max_active_runs=1,
    max_active_tasks=1,
    default_args=default_args,
    tags=["ddp"]
)


def _download_failed_banks_info(url: str, download_dir: str, **context):

    downloaded_filename = "failed_banks.csv." + datetime.datetime.now().strftime('%m_%d_%Y_%I_%M_%S_%p_unprocessed')

    response = requests.get(url, verify=False)

    file_info = {
        "url": url,
        "download_dir": download_dir,
        "filename": downloaded_filename,
        "http_status": response.status_code
    }

    if response.status_code == 200:
        with open(downloaded_filename, "wb") as file:
            file.write(response.content)

        shutil.move(downloaded_filename, download_dir)

        print(f"File has been downloaded and saved as {downloaded_filename} in {download_dir}")
    else:
        print(f"Failed to download the file: {url}. Status code: {response.status_code}")

    context['ti'].xcom_push(key='file_info', value=json.dumps(file_info))


download_failed_banks_file = PythonOperator(
    task_id="download_failed_banks_file",
    python_callable=_download_failed_banks_info,
    op_kwargs={
        "url": "https://www.fdic.gov/bank/individual/failed/banklist.csv",
        "download_dir": "/app/data/landing/"
    },
    dag=dag
)


ddp_rest_api_file_info = SimpleHttpOperator(
    task_id='ddp_rest_api_failed_bank_file_info',
    method='POST',
    http_conn_id='ddp-rest-api-conn',
    endpoint='/bank/fileInfo',
    data="{{ task_instance.xcom_pull(task_ids='download_failed_banks_file', key='file_info') }}",
    headers={"Content-Type": "application/json"},
    do_xcom_push=False,
    dag=dag)

download_failed_banks_file >> ddp_rest_api_file_info
