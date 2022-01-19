create table account (
    account_id varchar(10) not null primary key ,
    customer_id varchar(10) not null,
    account_type varchar(10) not null,
    balance numeric not null
)