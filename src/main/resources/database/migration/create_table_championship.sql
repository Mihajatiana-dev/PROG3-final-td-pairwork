CREATE TYPE championship_enum AS ENUM ('LA_LIGA', 'PREMIER_LEAGUE', 'BUNDESLIGA', 'LIGUE_1', 'SERIE_A');

CREATE TABLE championship
(
    championship_id   UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    championship_name championship_enum NOT NULL,
    country           VARCHAR(200) NOT NULL
)

CREATE TYPE country_enum AS ENUM ('ENGLAND', 'SPAIN', 'GERMANY', 'FRANCE', 'ITALY');

CREATE TABLE championship (
    championship_id   UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    championship_name championship_enum NOT NULL,
    country           country_enum NOT NULL
);