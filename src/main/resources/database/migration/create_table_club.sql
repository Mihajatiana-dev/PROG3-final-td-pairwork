CREATE TABLE club
(
    club_id         UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    club_name       VARCHAR(200) UNIQUE NOT NULL,
    creation_date   TIMESTAMP           NOT NULL,
    acronym         VARCHAR(5)          NOT NULL,
    stadium_id      UUID NOT NULL UNIQUE REFERENCES stadium (stadium_id),
    coach_id        UUID NOT NULL REFERENCES coach (coach_id),
    championship_id UUID NOT NULL REFERENCES championship (championship_id)
)

CREATE TABLE club
(
    club_id         UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    club_name       VARCHAR(200) UNIQUE NOT NULL,
    creation_date   INTEGER NOT NULL,
    acronym         VARCHAR(5) NOT NULL,
    stadium_name    VARCHAR(200) NOT NULL,
    coach_id        UUID NOT NULL REFERENCES coach (coach_id),
    championship_id UUID NOT NULL REFERENCES championship (championship_id)
);