CREATE TABLE Product
(
    identifier bigint primary key generated always as identity,
    name       varchar(50)      NOT NULL,
    price      double not null
);
