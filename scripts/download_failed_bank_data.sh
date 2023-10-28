#!/bin/bash

failed_banks="failed_banks.csv"
failed_banks_new="failed_banks_new.csv"

datetime=$(date +"%Y_%m_%d_%H_%M_%S")

if [ -f /data/landing/"$failed_banks" ]
then
  curl -o /data/landing/"$failed_banks_new" https://www.fdic.gov/bank/individual/failed/banklist.csv
  archived="${failed_banks}_archived_${datetime}"
  mv /data/landing/"$failed_banks" /data/landing/"$archived"
  mv /data/landing/"$archived" /data/archive/
  mv /data/landing/"$failed_banks_new" /data/landing/"$failed_banks"

  echo "Successfully downloaded the new file and archived the data file on $datetime"
else
  curl -o /data/landing/"$failed_banks" https://www.fdic.gov/bank/individual/failed/banklist.csv

  echo "Successfully downloaded the new failed bank data file on $datetime"
fi
