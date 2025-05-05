CREATE TABLE goal
(
    goal_id   UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    player_id UUID    NOT NULL REFERENCES player (player_id),
    club_id   UUID REFERENCES club (club_id),
    match_id  UUID    NOT NULL REFERENCES match (match_id),
    minute    INTEGER NOT NULL,
    own_goal  BOOLEAN          DEFAULT FALSE,
    CHECK ( minute >= 90 )
)