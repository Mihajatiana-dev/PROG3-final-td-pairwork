CREATE TABLE match
(
    match_id     UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    home_club_id UUID NOT NULL REFERENCES club (club_id),
    away_club_id UUID NOT NULL REFERENCES club (club_id),
    match_date   TIMESTAMP NOT NULL,
    stadium_id   UUID NOT NULL REFERENCES stadium (stadium_id),
    season_id    UUID NOT NULL REFERENCES season (season_id),
    CHECK (home_club_id <> away_club_id)
)