CREATE TABLE Product
(
    identifier integer primary key generated always as identity,
    name       varchar(50)      NOT NULL,
    price      double precision not null
);
