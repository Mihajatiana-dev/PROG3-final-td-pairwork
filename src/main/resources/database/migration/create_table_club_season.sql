CREATE TABLE club_season
(
    club_id   UUID NOT NULL REFERENCES club (club_id),
    season_id UUID NOT NULL REFERENCES season (season_id),
    PRIMARY KEY (club_id, season_id)
);
