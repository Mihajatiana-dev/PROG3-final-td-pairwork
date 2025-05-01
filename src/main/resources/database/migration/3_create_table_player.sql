CREATE TYPE position_enum AS ENUM ('STRIKER', 'MIDFIELDER', 'DEFENSE', 'GOAL_KEEPER');

CREATE TABLE player
(
    player_id   UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    player_name Varchar(200)  NOT NULL,
    number      Int           NOT NULL,
    position    position_enum NOT NULL,
    nationality Varchar(100)  NOT NULL,
    age         Int           NOT NULL,
    club_id     UUID REFERENCES club (club_id),
    UNIQUE (number, club_id)
)
