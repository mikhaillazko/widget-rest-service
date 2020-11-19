DROP TABLE IF EXISTS WIDGETS;
CREATE SEQUENCE WIDGETS_SEQ;
CREATE TABLE WIDGETS
(
    ID        INT primary key,
    X         INT  not null,
    Y         INT  not null,
    Z         INT  not null,
    WIDTH     INT  not null,
    HEIGHT    INT  not null,
    UPDATEDAT DATE not null
);
CREATE INDEX Z_INDEX ON WIDGETS(Z);