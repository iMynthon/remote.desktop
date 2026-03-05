CREATE SEQUENCE remote_schema.remote_client_connection_id_seq START 10000;

CREATE TABLE remote_schema.remote_client(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name_client VARCHAR(100) UNIQUE NOT NULL,
    connection_id BIGINT DEFAULT nextval('remote_schema.remote_client_connection_id_seq') UNIQUE,
    password VARCHAR(100),
    name_pc VARCHAR(100),
    created TIMESTAMP default current_timestamp
);

CREATE TABLE remote_schema.remote_server(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    port INT UNIQUE NOT NULL,
    host VARCHAR(20) NOT NULL,
    name VARCHAR(100),
    created TIMESTAMP default current_timestamp,
    online boolean,
    remote_client_id UUID,
    CONSTRAINT fk_remote_client FOREIGN KEY (remote_client_id) REFERENCES remote_client(id)
);
