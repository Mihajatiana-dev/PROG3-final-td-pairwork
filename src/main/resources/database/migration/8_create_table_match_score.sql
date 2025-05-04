CREATE TABLE match_score
(
    match_score_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    match_id       UUID NOT NULL REFERENCES match (match_id),
    home_score     int  not null CHECK (home_score >= 0),
    away_score     int  not null CHECK (away_score >= 0),
    UNIQUE (match_id)
)