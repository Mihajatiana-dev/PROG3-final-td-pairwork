CREATE TABLE season
(
    season_id       UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    start_date      TIMESTAMP NOT NULL,
    end_date        TIMESTAMP NOT NULL,
    championship_id UUID      NOT NULL REFERENCES championship (championship_id),
    CHECK (start_date < end_date)
)