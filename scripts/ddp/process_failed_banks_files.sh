#!/usr/bin/env bash

_dependencies=$(echo /app/jars/ddp-spark/lib/*.jar | tr ' ' ',')

spark-submit \
  --master local[2] \
  --deploy-mode client \
  --jars $_dependencies \
  --class org.hrahman.ddp.ddpspark.failedbanks.FailedBanksFileProcessor /app/jars/ddp-spark/ddp-spark-1.0.jar