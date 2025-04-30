CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create table stadium (
    stadium_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    stadium_name VARCHAR(200) NOT NULL
)