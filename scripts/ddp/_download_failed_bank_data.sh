#!/usr/bin/env bash

failed_banks="failed_banks.csv"
failed_banks_new="failed_banks_new.csv"

datetime=$(date +"%Y_%m_%d_%H_%M_%S")

if [ -f /app/data/landing/failed_banks/"$failed_banks" ]
then
  curl -o /app/data/landing/failed_banks/"$failed_banks_new" https://www.fdic.gov/bank/individual/failed/banklist.csv
  archived="${failed_banks}_archived_${datetime}"
  mv /app/data/landing/failed_banks/"$failed_banks" /app/data/landing/failed_banks/"$archived"
  mv /app/data/landing/failed_banks/"$archived" /app/data/archive/failed_banks
  mv /app/data/landing/failed_banks/"$failed_banks_new" /app/data/landing/failed_banks/"$failed_banks"

  echo "Successfully downloaded the new file and archived the data file on $datetime"
else
  curl -o /app/data/landing/failed_banks/"$failed_banks" https://www.fdic.gov/bank/individual/failed/banklist.csv

  echo "Successfully downloaded the new failed bank data file on $datetime"
fi
