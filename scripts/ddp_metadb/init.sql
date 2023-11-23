
create schema if not exists ddp_schema;

set schema 'ddp_schema';

drop table if exists test_table;

create table if not exists test_table (

    id serial primary key,
    value varchar
);

