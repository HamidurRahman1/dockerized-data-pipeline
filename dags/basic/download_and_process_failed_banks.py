import datetime
import glob
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
    schedule_interval=None,
    catchup=False,
    max_active_runs=1,
    max_active_tasks=1,
    default_args=default_args,
    tags=["basic"]
)


def _download_failed_banks_info(url: str, download_dir: str):

    downloaded_filename = "failed_banks.csv." + datetime.datetime.now().strftime('%m_%d_%Y_%I_%M_%S_%p_unprocessed')

    response = requests.get(url, verify=False)

    if response.status_code == 200:
        with open(downloaded_filename, "wb") as file:
            file.write(response.content)

        shutil.move(downloaded_filename, download_dir)

        print(f"File has been downloaded and saved as {downloaded_filename} in {download_dir}")
    else:
        print(f"Failed to download the file: {url}. Status code: {response.status_code}")


def _process_failed_banks_data(downloaded_data_dir: str):
    print(downloaded_data_dir)
    unprocessed_files = glob.glob(downloaded_data_dir + '/failed_banks' + '*' + '_unprocessed')
    print("Total unprocessed files: " + str(unprocessed_files))

    if len(unprocessed_files) == 0:
        print("There are no unprocessed files.")
        return

    processed_files = []
    error_files = []

    for file in unprocessed_files:
        try:
            print(f"Starting to process {file}")
            with open(file, encoding='windows-1252', mode='r') as unprocessed_file:
                lines = unprocessed_file.readlines()
                for i in range(len(lines)):
                    # logic TBA
                    print("\t".join(lines[i].split(",")))
                print(f"Done processing {file}.")
        except Exception as ex:
            print(str(ex))
            error_files.append(file)
            continue
        processed_files.append(file)

    print(f"Following files were processed successfully - {processed_files}")
    print(f"Following files were unprocessed due to errors - {error_files}")


download_failed_banks_data = PythonOperator(
    task_id="download_failed_banks_data",
    python_callable=_download_failed_banks_info,
    op_kwargs={
        "url": "https://www.fdic.gov/bank/individual/failed/banklist.csv",
        "download_dir": "/data/landing/"
    },
    dag=dag
)

process_data = PythonOperator(
    task_id="process_failed_banks_data",
    python_callable=_process_failed_banks_data,
    op_kwargs={
        "downloaded_data_dir": "/data/landing/"
    },
    dag=dag
)

download_failed_banks_data >> process_data
