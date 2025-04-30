CREATE TYPE season_status_enum AS ENUM ('NOT_STARTED', 'STARTED', 'FINISHED');

CREATE TABLE season
(
    season_id UUID PRIMARY KEY            DEFAULT uuid_generate_v4(),
    year      INTEGER            NOT NULL,
    alias     VARCHAR(200)       NOT NULL,
    status    season_status_enum NOT NULL DEFAULT 'NOT_STARTED',
    CHECK (year > 0)
);