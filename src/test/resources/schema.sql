create table if not exists contact
(
    id int auto_increment primary key,
    first_name       varchar(50) not null,
    last_name        varchar(50),
    email            varchar(50) not null,
    phone            varchar(50),
    address_line1    varchar(50) not null,
    address_line2    varchar(50) not null,
    address_line3    varchar(50) not null,
    message          clob(10k)  not null,
    insert_date_time timestamp
);
