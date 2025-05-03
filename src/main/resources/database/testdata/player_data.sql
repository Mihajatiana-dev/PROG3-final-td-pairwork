INSERT INTO player (player_id, player_name, number, position, nationality, age, club_id) VALUES
(uuid_generate_v4(), 'Lamine Yamal', 19, 'STRIKER', 'Spain', 17, (SELECT club_id FROM club WHERE club_name = 'FC Barcelone')),
(uuid_generate_v4(), 'Ferran Torres', 7, 'STRIKER', 'Spain', 25, (SELECT club_id FROM club WHERE club_name = 'FC Barcelone')),
(uuid_generate_v4(), 'Marc-André ter Stegen', 1, 'GOAL_KEEPER', 'Germany', 31, (SELECT club_id FROM club WHERE club_name = 'FC Barcelone')),
(uuid_generate_v4(), 'Ronald Araújo', 4, 'DEFENSE', 'Uruguay', 24, (SELECT club_id FROM club WHERE club_name = 'FC Barcelone')),
(uuid_generate_v4(), 'Jules Koundé', 23, 'DEFENSE', 'France', 24, (SELECT club_id FROM club WHERE club_name = 'FC Barcelone')),
(uuid_generate_v4(), 'Alejandro Balde', 12, 'DEFENSE', 'Spain', 19, (SELECT club_id FROM club WHERE club_name = 'FC Barcelone')),
(uuid_generate_v4(), 'Pedri', 8, 'MIDFIELDER', 'Spain', 20, (SELECT club_id FROM club WHERE club_name = 'FC Barcelone')),
(uuid_generate_v4(), 'Gavi', 6, 'MIDFIELDER', 'Spain', 19, (SELECT club_id FROM club WHERE club_name = 'FC Barcelone')),
(uuid_generate_v4(), 'Frenkie de Jong', 21, 'MIDFIELDER', 'Netherlands', 26, (SELECT club_id FROM club WHERE club_name = 'FC Barcelone')),
(uuid_generate_v4(), 'Robert Lewandowski', 9, 'STRIKER', 'Poland', 35, (SELECT club_id FROM club WHERE club_name = 'FC Barcelone')),
(uuid_generate_v4(), 'Raphinha', 11, 'STRIKER', 'Brazil', 27, (SELECT club_id FROM club WHERE club_name = 'FC Barcelone'));


INSERT INTO player (player_id, player_name, number, position, nationality, age, club_id) VALUES (uuid_generate_v4(), 'test', 11, 'STRIKER', 'Brazil', 27, null);