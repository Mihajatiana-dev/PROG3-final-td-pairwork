CREATE TABLE player_stat (
                             player_stat_id UUID PRIMARY KEY DEFAULT uuid_generate_v4() ,
                             goal_scored    Int NOT NULL ,
                             playing_time   decimal NOT NULL ,
                             player_id      UUID NOT NULL REFERENCES player(player_id)
)