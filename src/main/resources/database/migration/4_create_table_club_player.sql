CREATE TABLE club_player
(
    club_player_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    player_id      UUID NOT NULL REFERENCES player (player_id),
    club_id        UUID NOT NULL REFERENCES club (club_id)
)