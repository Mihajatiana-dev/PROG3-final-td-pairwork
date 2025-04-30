CREATE TABLE club_statistics
(
    club_statistics_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    club_id            UUID NOT NULL REFERENCES club (club_id),
    ranking_point      Int  NOT NULL,
    scored_goal        Int  NOT NULL,
    conceded_goal      Int  NOT NULL,
    difference_goal    Int  NOT NULL,
    clean_sheet_number Int  NOT NULL
)
-- may delete later