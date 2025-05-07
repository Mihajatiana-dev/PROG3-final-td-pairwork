CREATE TABLE transfert (
                           transfert_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                           player_id UUID NOT NULL REFERENCES player (player_id),
                           club_id UUID NOT NULL REFERENCES club (club_id),
                           status VARCHAR(10) NOT NULL CHECK (status IN ('in', 'out')),
                           transfert_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);