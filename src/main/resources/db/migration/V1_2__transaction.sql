create table transactions.tb_transaction (
    id bigserial not null,
    code varchar(255) not null,
    vehicle_code varchar(255) not null,
    client_code varchar(255) not null,
    car_seller_code varchar(255) not null,
    price numeric(19,2) not null,
    transaction_status varchar(255) not null,
    transaction_type_id bigint not null,
    create_by varchar(255) not null,
    created_date timestamp(6) not null,
    last_modified_by varchar(255),
    last_modified_date timestamp(6),
    status varchar(255) not null,
    primary key (id),
    CONSTRAINT fk_transaction_type FOREIGN KEY (transaction_type_id) REFERENCES transactions.tb_transaction_type(id)
);

CREATE UNIQUE INDEX constraint_vehicle_code ON transactions.tb_transaction(vehicle_code);
--CREATE UNIQUE INDEX constraint_car_seller_code ON transactions.tb_transaction(car_seller_code);
--CREATE UNIQUE INDEX constraint_client_code ON transactions.tb_transaction(client_code);
