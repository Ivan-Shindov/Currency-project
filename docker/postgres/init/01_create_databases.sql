CREATE DATABASE "currency-db";
CREATE USER currency_service WITH PASSWORD 'superpass';
CREATE USER currency_service_migration_executor WITH PASSWORD 'userthebest098';

CREATE DATABASE "websocket-db";
CREATE USER websocket_service WITH PASSWORD 'superpass2';
CREATE USER websocket_service_migration_executor WITH PASSWORD 'superpass2345';

\c "currency-db" root
GRANT ALL PRIVILEGES ON DATABASE "currency-db" TO currency_service;
GRANT ALL PRIVILEGES ON DATABASE "currency-db" TO currency_service_migration_executor;

GRANT ALL ON SCHEMA public TO currency_service;
GRANT ALL ON SCHEMA public TO currency_service_migration_executor;

\c "websocket-db" root
GRANT ALL PRIVILEGES ON DATABASE "websocket-db" TO websocket_service;
GRANT ALL PRIVILEGES ON DATABASE "websocket-db" TO websocket_service_migration_executor;

GRANT ALL ON SCHEMA public TO websocket_service;
GRANT ALL ON SCHEMA public TO websocket_service_migration_executor;