
create schema if not exists ddp_schema;

set schema 'ddp_schema';

drop table if exists test_table;

create table if not exists test_table (
    id serial primary key,
    value varchar
);

drop table if exists bank_file_info;

create table if not exists failed_bank_file_info (
    id serial primary key,
    url varchar,
    download_dir varchar,
    filename varchar,
    http_code integer
);

