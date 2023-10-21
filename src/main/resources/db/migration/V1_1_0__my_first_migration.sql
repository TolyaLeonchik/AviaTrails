CREATE SEQUENCE user_info_id_seq START 1;
CREATE SEQUENCE airlines_id_seq START 1;
CREATE SEQUENCE airports_id_seq START 1;
CREATE SEQUENCE payment_history_id_seq START 1;
CREATE SEQUENCE security_credentials_id_seq START 1;
CREATE SEQUENCE ticket_table_id_seq START 1;
CREATE SEQUENCE travel_table_id_seq START 1;

create sequence travel_table_id_seq;

alter sequence travel_table_id_seq owner to postgres;

create table airports
(
    id           bigserial
        constraint aero_ports_pkey
            primary key,
    port_name    varchar not null
        constraint aero_ports_port_name_key
            unique,
    city         varchar not null,
    country      varchar not null,
    airport_code varchar not null
);

alter table airports
    owner to postgres;

create table airlines
(
    id           bigserial
        primary key,
    airline_name varchar not null
        unique,
    country      varchar not null,
    port_id      bigint
        constraint airlines_aero_ports_id_fk
            references airports
            on update cascade on delete cascade
);

alter table airlines
    owner to postgres;

create table flight_table
(
    id                   bigint  default nextval('travel_table_id_seq'::regclass) not null
        constraint travel_tabel_pkey
            primary key,
    airline_id           bigint                                                   not null
        constraint travel_tabel_airlines_id_fk
            references airlines
            on update cascade on delete cascade,
    from_airport_id      bigint                                                   not null
        constraint travel_tabel_airports_id_fk
            references airports,
    to_airport_id        bigint                                                   not null
        constraint travel_tabel_airports_id_fk_2
            references airports,
    departure_time       timestamp                                                not null,
    arrival_time         timestamp                                                not null,
    flight_price         integer default 0                                        not null,
    number_of_free_seats integer                                                  not null
);

alter table flight_table
    owner to postgres;

alter sequence travel_table_id_seq owned by flight_table.id;

create table user_info
(
    id           bigserial
        primary key,
    first_name   varchar not null,
    last_name    varchar not null,
    phone_number varchar not null
        unique,
    email        varchar not null
);

alter table user_info
    owner to postgres;

create table ticket_table
(
    id                      bigserial
        primary key,
    flight_id               bigint                not null
        constraint ticket_table_travel_tabel_id_fk
            references flight_table
            on update cascade on delete cascade,
    passenger_id            bigint                not null
        constraint ticket_table_user_info_id_fk
            references user_info
            on update cascade on delete cascade,
    seat_number             integer default 0     not null,
    ticket_price            integer default 0     not null,
    number_of_tickets       integer default 1,
    active_status           boolean default false not null,
    booking_expiration_time timestamp             not null
);

alter table ticket_table
    owner to postgres;

create table security_credentials
(
    id            bigserial
        primary key,
    user_login    varchar not null
        unique,
    user_password varchar not null,
    user_role     varchar not null,
    user_id       bigint  not null
        constraint security_credentials_user_info_id_fk
            references user_info
            on update cascade on delete cascade
);

alter table security_credentials
    owner to postgres;

create table payment_history
(
    id           bigserial
        primary key,
    ticket_id    bigint
        unique
        constraint payment_history_ticket_table_id_fk
            references ticket_table
            on update cascade on delete cascade,
    passenger_id bigint
        constraint payment_history_user_info_id_fk
            references user_info,
    card_number  varchar
);

alter table payment_history
    owner to postgres;
