INSERT INTO club (club_id, club_name, acronym,year_creation, stadium, coach_id) VALUES
-- La Liga
(uuid_generate_v4(), 'Real Madrid FC',  'RMA', 1902,'Santiago Bernabeu', (SELECT coach_id FROM coach WHERE coach_name = 'Carlo Ancelotti')),
(uuid_generate_v4(), 'FC Barcelone',  'FCB', 1899,'Lluís Companys', (SELECT coach_id FROM coach WHERE coach_name = 'Hansi Flick')),
-- Premier League
(uuid_generate_v4(), 'Manchester City',  'MCI', 1880,'Etihad Stadium', (SELECT coach_id FROM coach WHERE coach_name = 'Pep Guardiola')),
-- Ligue 1
(uuid_generate_v4(), 'Paris Saint Germain',  'PSG', 1970,'Parc des Princes', (SELECT coach_id FROM coach WHERE coach_name = 'Christophe Galtier')),
-- Bundesliga
(uuid_generate_v4(), 'FC Bayern Munich', 'FCB', 1900, 'Allianz Arena', (SELECT coach_id FROM coach WHERE coach_name = 'Julian Nagelsmann')),
-- Serie A
(uuid_generate_v4(), 'Juventus FC',  'JUV', 1897,'Juventus Stadium', (SELECT coach_id FROM coach WHERE coach_name = 'Massimiliano Allegri'));