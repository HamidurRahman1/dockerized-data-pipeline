import datetime
from airflow import DAG

from airflow.operators.bash import BashOperator
from airflow.operators.python import PythonOperator

default_args = {
    "owner": "Hamid",
    "retries": 1,
    "retry_delay": datetime.timedelta(minutes=5)
}

dag = DAG(
    dag_id="print_datetime_differently",
    start_date=datetime.datetime.now() - datetime.timedelta(days=1),
    schedule_interval=datetime.timedelta(days=1),
    catchup=False,
    max_active_runs=1,
    max_active_tasks=1,
    default_args=default_args,
    tags=["basic"]
)


def print_datetime():
    dt = f"Python datetime: {datetime.datetime.now().strftime('%m-%d-%Y %I:%M:%S %p')}"
    print(dt)
    return dt


print_datetime_using_python = PythonOperator(
    task_id="print_datetime_using_python_operator",
    python_callable=print_datetime,
    dag=dag
)

print_date_using_bash_op = BashOperator(
    task_id="print_datetime_using_bash_operator",
    bash_command="echo `date +%m-%d-%Y`",
    dag=dag
)

print_datetime_using_python >> print_date_using_bash_op
