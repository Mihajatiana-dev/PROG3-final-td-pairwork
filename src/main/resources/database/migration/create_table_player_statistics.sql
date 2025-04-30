CREATE TABLE player_statistics
(
    player_statistics_id  UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    scored_goal     Int  NOT NULL,
    playing_time_id UUID NOT NULL REFERENCES playing_time (playing_time_id)
)
-- may delete later