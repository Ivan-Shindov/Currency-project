create table public.currency_rate
(
    id           bigint not null,
    ratio        integer,
    code         varchar(3),
    rate         numeric(33, 18),
    reverse_rate numeric(33, 18),
    version      integer not null,
    date         timestamp(6) without time zone,
    primary key (id)
);

ALTER TABLE public.currency_rate OWNER TO websocket_service_migration_executor;

create table public.currency_rate_name
(
    currency_rate_id bigint  not null,
    language_id      int     not null,
    name             varchar not null,
    version          integer not null
);

ALTER TABLE public.currency_rate_name OWNER TO websocket_service_migration_executor;

ALTER TABLE ONLY public.currency_rate_name
    ADD CONSTRAINT currency_name_pkey
    PRIMARY KEY (currency_rate_id, language_id);

ALTER TABLE ONLY public.currency_rate_name
    ADD CONSTRAINT fk_name_currency
    FOREIGN KEY (currency_rate_id)
    REFERENCES currency_rate (id)
    ON DELETE CASCADE;

CREATE SEQUENCE public.currency_rate_seq
    START WITH 1
    INCREMENT BY 10
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.currency_rate_seq OWNER TO websocket_service_migration_executor;

GRANT ALL ON SCHEMA public TO websocket_service_migration_executor;
GRANT ALL ON SCHEMA public TO websocket_service;

GRANT ALL ON TABLE public.currency_rate TO websocket_service;
GRANT ALL ON SEQUENCE public.currency_rate_seq TO websocket_service;

GRANT ALL ON TABLE public.currency_rate_name TO websocket_service;