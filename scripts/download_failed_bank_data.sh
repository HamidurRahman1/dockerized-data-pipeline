#!/bin/bash

failed_banks="failed_banks.csv"

curl -o /data/landing/"$failed_banks" https://www.fdic.gov/bank/individual/failed/banklist.csv

echo "Successfully downloaded the failed bank data file."
