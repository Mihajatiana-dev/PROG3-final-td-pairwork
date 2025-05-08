-- Since coaches aren't in the Excel, I'll create placeholder coaches
INSERT INTO coach (coach_name, nationality)
VALUES ('Coach Club 1', 'French'),
       ('Coach Club 2', 'Spanish'),
       ('Coach Club 3', 'German');


-- Insert clubs with their coaches
INSERT INTO club (club_name, acronym, year_creation, stadium, coach_id)
VALUES ('Club 1', 'C1', 1902, 'Stade 1', (SELECT coach_id FROM coach WHERE coach_name = 'Coach Club 1')),
       ('Club 2', 'C2', 1905, 'Stade 2', (SELECT coach_id FROM coach WHERE coach_name = 'Coach Club 2')),
       ('Club 3', 'C3', 1910, 'Stade 3', (SELECT coach_id FROM coach WHERE coach_name = 'Coach Club 3'));


-- Club 1 players
INSERT INTO player (player_name, number, position, nationality, age, club_id)
VALUES ('Gardien 1', 1, 'GOAL_KEEPER', 'French', 30, (SELECT club_id FROM club WHERE club_name = 'Club 1')),
       ('Défense 1', 2, 'DEFENSE', 'French', 25, (SELECT club_id FROM club WHERE club_name = 'Club 1')),
       ('Milieu 1', 5, 'MIDFIELDER', 'French', 24, (SELECT club_id FROM club WHERE club_name = 'Club 1')),
       ('Attaquant 1', 7, 'STRIKER', 'French', 17, (SELECT club_id FROM club WHERE club_name = 'Club 1'));

-- Club 2 players
INSERT INTO player (player_name, number, position, nationality, age, club_id)
VALUES ('Gardien 2', 1, 'GOAL_KEEPER', 'Spanish', 21, (SELECT club_id FROM club WHERE club_name = 'Club 2')),
       ('Défense 2', 2, 'DEFENSE', 'Spanish', 34, (SELECT club_id FROM club WHERE club_name = 'Club 2')),
       ('Milieu 2', 5, 'MIDFIELDER', 'Spanish', 29, (SELECT club_id FROM club WHERE club_name = 'Club 2')),
       ('Attaquant 2', 7, 'STRIKER', 'Spanish', 18, (SELECT club_id FROM club WHERE club_name = 'Club 2'));

-- Club 3 players
INSERT INTO player (player_name, number, position, nationality, age, club_id)
VALUES ('Gardien 3', 1, 'GOAL_KEEPER', 'German', 28, (SELECT club_id FROM club WHERE club_name = 'Club 3')),
       ('Défense 3', 2, 'DEFENSE', 'German', 21, (SELECT club_id FROM club WHERE club_name = 'Club 3')),
       ('Milieu 3', 5, 'MIDFIELDER', 'German', 29, (SELECT club_id FROM club WHERE club_name = 'Club 3')),
       ('Attaquant 3', 7, 'STRIKER', 'German', 23, (SELECT club_id FROM club WHERE club_name = 'Club 3'));


INSERT INTO season (alias, status, year)
VALUES ('Saison 2024', 'FINISHED', 2024),
       ('Saison 2025', 'NOT_STARTED', 2025);


