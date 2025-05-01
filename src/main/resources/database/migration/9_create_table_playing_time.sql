CREATE TYPE duration_unit_enum AS ENUM ('SECOND', 'MINUTE', 'HOUR');

CREATE TABLE playing_time
(
    playing_time_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    value           INTEGER            NOT NULL,
    duration_unit   duration_unit_enum NOT NULL
)
-- may delete later