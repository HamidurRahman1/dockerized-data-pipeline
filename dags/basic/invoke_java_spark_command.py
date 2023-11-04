import datetime
from airflow import DAG

from airflow.operators.empty import EmptyOperator
from airflow.operators.bash import BashOperator

default_args = {
    "owner": "Hamid",
    "retries": 1,
    "retry_delay": datetime.timedelta(minutes=5)
}

dag = DAG(
    dag_id="check_java_spark_versions",
    start_date=datetime.datetime.now() - datetime.timedelta(days=1),
    schedule_interval=datetime.timedelta(days=1),
    catchup=False,
    max_active_runs=1,
    max_active_tasks=3,
    default_args=default_args,
    tags=["basic"]
)

start = EmptyOperator(
    task_id="start",
    dag=dag
)


java_version = BashOperator(
    task_id="check_java_version",
    bash_command="echo `java -version`",
    dag=dag
)

spark_shell_version = BashOperator(
    task_id="check_spark_shell_version",
    bash_command="echo `spark-shell --version`",
    dag=dag
)

spark_submit_version = BashOperator(
    task_id="check_spark_submit_version",
    bash_command="echo `spark-submit --version`",
    dag=dag
)

start >> [java_version, spark_shell_version, spark_submit_version]
