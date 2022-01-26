create sequence hibernate_sequence start with 1 increment by 1;
create table transaction (
     transaction_id numeric not null primary key ,
     account_id varchar(10) not null,
     amount numeric not null,
     balance numeric not null,
     description varchar(100) not null,
     transaction_type varchar(10) not null,
     transaction_date timestamp not null
);


