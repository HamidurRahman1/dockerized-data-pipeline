import json
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
    dag_id="basic.called_via_rest_api",
    start_date=datetime.datetime.now() - datetime.timedelta(days=1),
    is_paused_upon_creation=False,
    schedule_interval=None,
    catchup=False,
    max_active_runs=1,
    max_active_tasks=1,
    default_args=default_args,
    tags=["ddp"]
)


def print_all_contexts_info(**context):

    rest_conf_body = context['dag_run'].conf

    print("param1:", rest_conf_body.get("param1", "default_param1_value"))
    print("param2:", rest_conf_body.get("param2", "default_param2_value"))

    context['ti'].xcom_push(key='api_params', value=json.dumps({
        "param1": rest_conf_body.get("param1", "default_param1_value"),
        "param2": rest_conf_body.get("param2", "default_param2_value")
    }))

    print(context)


retrieve_conf_body = PythonOperator(
    task_id="retrieve_conf_body",
    python_callable=print_all_contexts_info,
    do_xcom_push=True,
    dag=dag
)

print_using_templating = BashOperator(
    task_id="print_using_templating",
    bash_command="echo {{ ti.xcom_pull(task_ids='retrieve_conf_body', key='api_params') }}",
    do_xcom_push=False,
    dag=dag
)

retrieve_conf_body >> print_using_templating
