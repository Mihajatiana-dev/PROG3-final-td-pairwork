CREATE TABLE club
(
    club_id       UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    club_name     VARCHAR(200) UNIQUE NOT NULL,
    acronym       VARCHAR(5)          NOT NULL,
    year_creation INTEGER             NOT NULL,
    stadium       VARCHAR(200)        NOT NULL,
    coach_id      UUID REFERENCES coach (coach_id),
    UNIQUE (club_id, coach_id)
);