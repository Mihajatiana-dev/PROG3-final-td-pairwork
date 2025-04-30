CREATE TABLE season_championship
(
    chamionship_id UUID NOT NULL REFERENCES championship(championship_id),
    season_id      UUID NOT NULL REFERENCES season(season_id)
)