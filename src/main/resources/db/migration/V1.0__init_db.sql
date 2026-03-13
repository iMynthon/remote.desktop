CREATE SEQUENCE remote_schema.remote_client_connection_id_seq START 10000;

CREATE TABLE remote_schema.remote_client(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name_client VARCHAR(100) UNIQUE NOT NULL,
    connection_id BIGINT DEFAULT nextval('remote_schema.remote_client_connection_id_seq') UNIQUE,
    password VARCHAR(100),
    created TIMESTAMP default current_timestamp
);

CREATE TABLE remote_schema.server_log_connection(
     id BIGSERIAL PRIMARY KEY,
     client_id UUID NOT NULL,
     connection_id BIGINT NOT NULL,
     connection_create_time TIMESTAMP default current_timestamp
)


