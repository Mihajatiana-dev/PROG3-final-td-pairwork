CREATE TYPE duration_unit_enum AS ENUM ('SECOND', 'MINUTE', 'HOUR');

CREATE TABLE playing_time
(
    id            UUID PRIMARY KEY            DEFAULT uuid_generate_v4(),
    player_id     UUID               NOT NULL REFERENCES player (player_id),
    match_id      UUID               NOT NULL REFERENCES match (match_id),
    value         DOUBLE PRECISION   NOT NULL,
    duration_unit duration_unit_enum NOT NULL DEFAULT 'MINUTE',
    CHECK (value >= 0 AND duration_unit IN ('SECOND', 'MINUTE', 'HOUR'))
)