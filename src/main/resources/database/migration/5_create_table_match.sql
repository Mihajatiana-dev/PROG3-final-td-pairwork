CREATE TYPE match_status_enum AS ENUM ('NOT_STARTED', 'STARTED', 'FINISHED');

CREATE TABLE match
(
    match_id       UUID PRIMARY KEY           DEFAULT uuid_generate_v4(),
    home_club_id   UUID              NOT NULL REFERENCES club (club_id),
    away_club_id   UUID              NOT NULL REFERENCES club (club_id),
    stadium        VARCHAR(200)      NOT NULL,
    match_datetime TIMESTAMP,
    status         match_status_enum NOT NULL DEFAULT 'NOT_STARTED',
    season_id      UUID REFERENCES season (season_id),
    CHECK (home_club_id <> away_club_id)
)