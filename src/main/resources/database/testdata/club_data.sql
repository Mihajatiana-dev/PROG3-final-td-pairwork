INSERT INTO club (club_id, club_name, creation_date, acronym, stadium_name, coach_id, championship_id) VALUES
-- La Liga
(uuid_generate_v4(), 'Real Madrid FC', 1902, 'RMA', 'Santiago Bernabeu', (SELECT coach_id FROM coach WHERE coach_name = 'Carlo Ancelotti'), (SELECT championship_id FROM championship WHERE championship_name = 'LA_LIGA')),
(uuid_generate_v4(), 'FC Barcelone', 1899, 'FCB', 'Lluís Companys', (SELECT coach_id FROM coach WHERE coach_name = 'Hansi Flick'), (SELECT championship_id FROM championship WHERE championship_name = 'LA_LIGA')),
-- Premier League
(uuid_generate_v4(), 'Manchester City', 1880, 'MCI', 'Etihad Stadium', (SELECT coach_id FROM coach WHERE coach_name = 'Pep Guardiola'), (SELECT championship_id FROM championship WHERE championship_name = 'PREMIER_LEAGUE')),
-- Ligue 1
(uuid_generate_v4(), 'Paris Saint Germain', 1970, 'PSG', 'Parc des Princes', (SELECT coach_id FROM coach WHERE coach_name = 'Christophe Galtier'), (SELECT championship_id FROM championship WHERE championship_name = 'LIGUE_1')),
-- Bundesliga
(uuid_generate_v4(), 'FC Bayern Munich', 1900, 'FCB', 'Allianz Arena', (SELECT coach_id FROM coach WHERE coach_name = 'Julian Nagelsmann'), (SELECT championship_id FROM championship WHERE championship_name = 'BUNDESLIGA')),
-- Serie A
(uuid_generate_v4(), 'Juventus FC', 1897, 'JUV', 'Juventus Stadium', (SELECT coach_id FROM coach WHERE coach_name = 'Massimiliano Allegri'), (SELECT championship_id FROM championship WHERE championship_name = 'SERIE_A'));