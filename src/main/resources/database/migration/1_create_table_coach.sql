CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE coach
(
    coach_id    UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    coach_name  Varchar(200) NOT NULL,
    nationality Varchar(100) NOT NULL
)