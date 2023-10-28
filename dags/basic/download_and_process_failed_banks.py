import datetime
from airflow import DAG

from airflow.operators.bash import BashOperator

default_args = {
    "owner": "Hamid",
    "retries": 1,
    "retry_delay": datetime.timedelta(minutes=1)
}

dag = DAG(
    dag_id="download_and_process_failed_banks",
    start_date=datetime.datetime.now() - datetime.timedelta(days=1),
    template_searchpath="/scripts",
    # schedule_interval=datetime.timedelta(minutes=3),
    schedule_interval=None,
    catchup=False,
    max_active_runs=1,
    max_active_tasks=1,
    default_args=default_args
)


download_failed_banks_data = BashOperator(
    task_id="download_failed_banks_data",
    bash_command="/download_failed_bank_data.sh",
    dag=dag
)

download_failed_banks_data

