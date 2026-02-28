CREATE TABLE remote_schema.remote_client(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name_client VARCHAR(100) NOT NULL,
    name_pc VARCHAR(100),
    created TIMESTAMP default current_timestamp
);

CREATE TABLE remote_schema.remote_server(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    port INT NOT NULL,
    host VARCHAR(20) NOT NULL,
    name VARCHAR(100),
    created TIMESTAMP default current_timestamp,
    online boolean
);