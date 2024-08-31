create table transactions.tb_transaction (
    id bigserial not null,
    code varchar(255) not null,
    vehicle_code varchar(255) not null,
    client_code varchar(255) not null,
    price numeric(19,2) not null,
    transaction_status varchar(255) not null,
    transaction_type_id bigint not null,
    create_by varchar(255) not null,
    created_date timestamp(6) not null,
    last_modified_by varchar(255),
    last_modified_date timestamp(6),
    status varchar(255) not null,
    primary key (id)
);