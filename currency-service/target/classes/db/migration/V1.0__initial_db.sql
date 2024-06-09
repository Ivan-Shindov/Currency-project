create table public.currency
(
    id           bigint not null,
    ratio        integer,
    code         varchar(3),
    rate         numeric(33, 18),
    reverse_rate numeric(33, 18),
    version      integer not null,
    created_on   timestamp without time zone,
    updated_on   timestamp without time zone,
    date         timestamp(6) without time zone,
    created_by   varchar(255),
    source       varchar(20),
    primary key (id)
);

ALTER TABLE public.currency OWNER TO currency_service_migration_executor;

create table public.chronological_exchange_rate
(
    id            bigint  not null,
    currency_id   bigint,
    created_on    timestamp without time zone,
    rate          numeric(33, 18),
    reverse_rate  numeric(33, 18),
    primary key (id),
    CONSTRAINT fk_currency
        FOREIGN KEY (currency_id)
            REFERENCES currency (id)
            ON DELETE CASCADE
);

ALTER TABLE public.chronological_exchange_rate OWNER TO currency_service_migration_executor;

create table public.currency_name
(
    currency_id bigint  not null,
    language_id int     not null,
    name        varchar not null,
    created_on  timestamp without time zone,
    version     integer not null
);

ALTER TABLE public.currency_name OWNER TO currency_service_migration_executor;

ALTER TABLE ONLY public.currency_name
    ADD CONSTRAINT currency_name_pkey
        PRIMARY KEY (currency_id, language_id);

ALTER TABLE ONLY public.currency_name
    ADD CONSTRAINT fk_name_currency
        FOREIGN KEY (currency_id)
            REFERENCES currency (id)
            ON DELETE CASCADE;

CREATE SEQUENCE public.currency_seq
    START WITH 1
    INCREMENT BY 10
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.currency_seq OWNER TO currency_service_migration_executor;

CREATE SEQUENCE public.chronological_exchange_rate_seq
    START WITH 1
    INCREMENT BY 10
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.chronological_exchange_rate_seq OWNER TO currency_service_migration_executor;

GRANT ALL ON SCHEMA public TO currency_service_migration_executor;
GRANT ALL ON SCHEMA public TO currency_service;

GRANT ALL ON TABLE public.currency TO currency_service;
GRANT ALL ON SEQUENCE public.currency_seq TO currency_service;

GRANT ALL ON TABLE public.chronological_exchange_rate TO currency_service;
GRANT ALL ON SEQUENCE public.chronological_exchange_rate_seq TO currency_service;

GRANT ALL ON TABLE public.currency_name TO currency_service;