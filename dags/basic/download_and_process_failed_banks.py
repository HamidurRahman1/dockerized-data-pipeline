import datetime
import requests
import shutil
from airflow import DAG

from airflow.operators.python import PythonOperator

default_args = {
    "owner": "Hamid",
    "retries": 1,
    "retry_delay": datetime.timedelta(minutes=1)
}

dag = DAG(
    dag_id="download_and_process_failed_banks",
    start_date=datetime.datetime.now() - datetime.timedelta(days=1),
    # template_searchpath="/scripts",
    schedule_interval=datetime.timedelta(weeks=1),
    catchup=False,
    max_active_runs=1,
    max_active_tasks=1,
    default_args=default_args
)


def _download_failed_banks_info(url, download_dir: str):

    downloaded_filename = "failed_banks.csv." + datetime.datetime.now().strftime('%m_%d_%Y_%I_%M_%S_%p_unprocessed')

    response = requests.get(url, verify=False)

    if response.status_code == 200:
        with open(downloaded_filename, "wb") as file:
            file.write(response.content)

        shutil.move(downloaded_filename, download_dir)

        print(f"File has been downloaded and saved as {downloaded_filename} in {download_dir}")
    else:
        print(f"Failed to download the file: {url}. Status code: {response.status_code}")


download_failed_banks_data = PythonOperator(
    task_id="download_failed_banks_data",
    python_callable=_download_failed_banks_info,
    op_kwargs={
        "url": "https://www.fdic.gov/bank/individual/failed/banklist.csv",
        "download_dir": "/data/landing/"
    },
    dag=dag
)

download_failed_banks_data

