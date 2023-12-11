
create schema if not exists ddp_schema;

set schema 'ddp_schema';

drop table if exists failed_bank_file_info;

create table if not exists failed_bank_file_info (
    id serial primary key,
    url varchar,
    download_dir varchar,
    filename varchar,
    http_code integer,
    processor_flag char(1),
    processed_dir varchar
);

create table if not exists nyc_violation_analytics (
    id serial primary key,
    input_file varchar,
    county_name varchar,
    county_violations numeric,
    precinct varchar,
    precinct_avg_fine numeric
);

