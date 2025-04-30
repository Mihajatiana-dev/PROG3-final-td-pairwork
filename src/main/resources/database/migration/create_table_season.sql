CREATE TABLE season
(
    season_id       UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    start_date      TIMESTAMP NOT NULL,
    end_date        TIMESTAMP NOT NULL,
    championship_id UUID      NOT NULL REFERENCES championship (championship_id),
    CHECK (start_date < end_date)
)


CREATE TYPE season_status_enum AS ENUM ('NOT_STARTED', 'STARTED', 'FINISHED');

CREATE TABLE season (
                        season_id       UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                        alias           VARCHAR(255) NOT NULL,
                        status          season_status_enum NOT NULL DEFAULT 'NOT_STARTED',
                        year            INTEGER NOT NULL,
                        CHECK (year > 0)
    );