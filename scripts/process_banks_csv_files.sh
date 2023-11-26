#!/bin/bash


spark-submit \
  --jars /app/jars/ddp-spark/lib/*:/app/jars/ddp-spark/ddp-spark-1.0.jar \
  --master local[2] \
  --deploy-mode client \
  --class org.hrahman.ddp.ddpspark.App /app/jars/ddp-spark/ddp-spark-1.0.jar


