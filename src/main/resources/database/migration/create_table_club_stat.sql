CREATE TABLE club_stat
(
    club_stat_id  UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    point         Int NOT NULL,
    goal_scored   Int NOT NULL,
    goal_conceded Int NOT NULL,
    clean_sheet   Int NOT NULL,
    club_id       UUID NOT NULL REFERENCES club (club_id)
)