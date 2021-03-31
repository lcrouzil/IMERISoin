-- auto-generated definition
create table drug
(
    id   INTEGER not null
        constraint drug_pk
            primary key,
    name text
);

create unique index drug_int_uindex
    on drug (id);

-- auto-generated definition
create table patient
(
    id     INTEGER not null
        constraint patient_pk
            primary key,
    name   text    not null,
    status text
);

create unique index patient_id_uindex
    on patient (id);

-- auto-generated definition
create table room
(
    id         INTEGER not null
        constraint room_pk
            primary key,
    patient_id INTEGER
        references patient,
    path       text,
    drug_id    INTEGER
        references drug
);

create unique index room_id_uindex
    on room (id);

create unique index room_patient_id_uindex
    on room (patient_id);

