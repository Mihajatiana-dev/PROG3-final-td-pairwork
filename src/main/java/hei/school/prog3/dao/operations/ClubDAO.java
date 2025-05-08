package hei.school.prog3.dao.operations;

import hei.school.prog3.config.DbConnection;
import hei.school.prog3.dao.mapper.ClubMapper;
import hei.school.prog3.model.Club;
import hei.school.prog3.model.Coach;
import hei.school.prog3.model.Player;
import hei.school.prog3.model.enums.PlayerPosition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ClubDAO implements GenericOperations<Club> {
    private final DbConnection dbConnection;
    private final ClubMapper clubMapper;
    private final CoachDAO coachDAO;

    public Club findClubByPlayerId(String playerId) {

        String query = """
                SELECT c.club_id, c.club_name, c.acronym, c.year_creation, c.stadium, c.coach_id
                FROM club c
                JOIN player p ON c.club_id = p.club_id
                WHERE p.player_id = ?::uuid
                """;

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.setString(1, playerId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return clubMapper.apply(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null; // null if not found club
    }

    @Override
    public List<Club> showAll(int page, int size) {
        List<Club> clubList = new ArrayList<>();
        String query = "SELECT c.* \n" +
                "FROM club c  LIMIT ? OFFSET ?";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.setInt(1, size);
            preparedStatement.setInt(2, size * (page - 1));
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Club club = clubMapper.apply(resultSet);
                    clubList.add(club);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            return clubList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Club> save(List<Club> clubs) {
        return null;
    }

    @Override
    public Club findById(String modelId) {
        String query = """
                SELECT club_id, club_name, acronym, year_creation, stadium, coach_id
                FROM club
                WHERE club_id = ?::uuid
                """;
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.setString(1, modelId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return clubMapper.apply(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null; // null if not found
    }


    public List<Club> saveAll(List<Club> clubToSave) {
        List<Club> clubList = new ArrayList<>();
        String upsertClubSql = """
                INSERT INTO club (club_id, club_name, acronym, year_creation, stadium, coach_id)
                VALUES (?, ?, ?, ?, ?, ?)
                ON CONFLICT (club_id) 
                DO UPDATE SET 
                    club_name = EXCLUDED.club_name,
                    acronym = EXCLUDED.acronym,
                    year_creation = EXCLUDED.year_creation,
                    stadium = EXCLUDED.stadium,
                    coach_id = EXCLUDED.coach_id
                RETURNING club_id, club_name, acronym, year_creation, stadium, coach_id
                """;

        String insertCoachSql = """
                INSERT INTO coach (coach_name, nationality)
                VALUES (?, ?)
                RETURNING coach_id, coach_name, nationality
                """;

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement clubStmt = connection.prepareStatement(upsertClubSql);
             PreparedStatement insertCoachStmt = connection.prepareStatement(insertCoachSql)) {

            for (Club request : clubToSave) {
                // manage the coach first
                Coach coachRequest = request.getCoach();
                // verify if the coach exists
                Coach coach = coachDAO.findByName(coachRequest.getName());

                // if not, we create a new coach
                if (coach == null) {
                    insertCoachStmt.setString(1, coachRequest.getName());
                    insertCoachStmt.setString(2, coachRequest.getNationality());

                    try (ResultSet coachRs = insertCoachStmt.executeQuery()) {
                        if (coachRs.next()) {
                            coach = new Coach();
                            coach.setId(coachRs.getString("coach_id"));
                            coach.setName(coachRs.getString("coach_name"));
                            coach.setNationality(coachRs.getString("nationality"));
                        }
                    }
                    insertCoachStmt.clearParameters();
                }

                // manage the club only if the coach is valid
                if (coach != null) {
                    clubStmt.setObject(1, request.getId() != null ?
                            UUID.fromString(request.getId()) : UUID.randomUUID(), Types.OTHER);     // generate UUID if null
                    clubStmt.setString(2, request.getName());
                    clubStmt.setString(3, request.getAcronym());
                    clubStmt.setInt(4, request.getYearCreation());
                    clubStmt.setString(5, request.getStadium());
                    clubStmt.setObject(6, UUID.fromString(coach.getId()), Types.OTHER);

                    try (ResultSet clubRs = clubStmt.executeQuery()) {
                        if (clubRs.next()) {
                            Club club = new Club(
                                    clubRs.getString("club_id"),
                                    clubRs.getString("club_name"),
                                    clubRs.getString("acronym"),
                                    clubRs.getInt("year_creation"),
                                    clubRs.getString("stadium"),
                                    coach
                            );
                            clubList.add(club);
                        }
                    }
                    clubStmt.clearParameters();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save clubs", e);
        }
        return clubList;
    }

    public Club getClubWithPlayers(String clubId) {
        String clubSql = "SELECT club_id, club_name, acronym, year_creation, stadium, coach_id FROM club WHERE club_id = ?";
        String playersSql = "SELECT player_id, player_name, number, position, nationality, age FROM player WHERE club_id = ?";
        String coachSql = "SELECT coach_id, coach_name, nationality FROM coach WHERE coach_id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement clubStmt = conn.prepareStatement(clubSql);
             PreparedStatement playersStmt = conn.prepareStatement(playersSql);
             PreparedStatement coachStmt = conn.prepareStatement(coachSql)) {
            clubStmt.setObject(1, UUID.fromString(clubId));
            ResultSet clubRs = clubStmt.executeQuery();

            if (!clubRs.next()) {
                return null;
            }

            UUID coachId = (UUID) clubRs.getObject("coach_id");
            coachStmt.setObject(1, coachId);
            ResultSet coachRs = coachStmt.executeQuery();

            Coach coach = null;
            if (coachRs.next()) {
                coach = new Coach(
                        coachRs.getString("coach_id"),
                        coachRs.getString("coach_name"),
                        coachRs.getString("nationality")
                );
            }

            Club club = new Club(
                    clubRs.getString("club_id"),
                    clubRs.getString("club_name"),
                    clubRs.getString("acronym"),
                    clubRs.getInt("year_creation"),
                    clubRs.getString("stadium"),
                    coach
            );

            playersStmt.setObject(1, UUID.fromString(clubId));
            ResultSet playersRs = playersStmt.executeQuery();

            List<Player> players = new ArrayList<>();
            while (playersRs.next()) {
                Player player = new Player(
                        playersRs.getString("player_id"),
                        playersRs.getString("player_name"),
                        playersRs.getInt("number"),
                        PlayerPosition.valueOf(playersRs.getString("position")),
                        playersRs.getString("nationality"),
                        playersRs.getInt("age")
                );
                players.add(player);
            }
            club.setPlayerList(players);
            return club;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch club with players", e);
        }
    }

    public List<Player> changePlayers(String clubId, List<Player> newPlayers) {
        String clearExistingPlayersSql = "UPDATE player SET club_id = NULL WHERE club_id = ?";
        String updatePlayerSql = "UPDATE player SET club_id = ? WHERE player_id = ?";
        String getPlayerSql = "SELECT player_id, player_name, number, position, nationality, age FROM player WHERE player_id = ?";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement clearStmt = connection.prepareStatement(clearExistingPlayersSql);
             PreparedStatement updateStmt = connection.prepareStatement(updatePlayerSql);
             PreparedStatement getPlayerStmt = connection.prepareStatement(getPlayerSql)) {

            connection.setAutoCommit(false);

            // detach club_id from existing players
            clearStmt.setObject(1, UUID.fromString(clubId), Types.OTHER);
            clearStmt.executeUpdate();

            // update players with new club_id
            List<Player> result = new ArrayList<>();

            for (Player player : newPlayers) {
                // Update player's club_id
                updateStmt.setObject(1, UUID.fromString(clubId), Types.OTHER);
                updateStmt.setObject(2, UUID.fromString(player.getId()), Types.OTHER);
                updateStmt.executeUpdate();
                updateStmt.clearParameters();

                // Get updated player
                getPlayerStmt.setObject(1, UUID.fromString(player.getId()), Types.OTHER);
                try (ResultSet rs = getPlayerStmt.executeQuery()) {
                    if (rs.next()) {
                        Player savedPlayer = new Player();
                        savedPlayer.setId(rs.getString("player_id"));
                        savedPlayer.setName(rs.getString("player_name"));
                        savedPlayer.setNumber(rs.getInt("number"));
                        savedPlayer.setPosition(PlayerPosition.valueOf(rs.getString("position")));
                        savedPlayer.setNationality(rs.getString("nationality"));
                        savedPlayer.setAge(rs.getInt("age"));
                        result.add(savedPlayer);
                    }
                }
                getPlayerStmt.clearParameters();
            }

            connection.commit();
            return result;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to replace club players", e);
        }
    }

    public List<Club> findAllClubs() {
        String sql = """
                SELECT c.club_id, c.club_name, c.acronym, c.year_creation, c.stadium, 
                       ch.coach_id, ch.coach_name, ch.nationality as coach_nationality
                FROM club c
                LEFT JOIN coach ch ON c.coach_id = ch.coach_id
                """;

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            List<Club> clubs = new ArrayList<>();
            while (rs.next()) {
                Club club = new Club(
                        rs.getString("club_id"),
                        rs.getString("club_name"),
                        rs.getString("acronym"),
                        rs.getInt("year_creation"),
                        rs.getString("stadium")
                );

                if (rs.getString("coach_id") != null) {
                    Coach coach = new Coach(
                            rs.getString("coach_id"),
                            rs.getString("coach_name"),
                            rs.getString("coach_nationality")
                    );
                    club.setCoach(coach);
                }

                clubs.add(club);
            }
            return clubs;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch clubs", e);
        }
    }

    public Integer getScoredGoals(String clubId) {
        String sql = """
                SELECT COALESCE(SUM(
                    CASE 
                        WHEN m.home_club_id = ? THEN ms.home_score
                        WHEN m.away_club_id = ? THEN ms.away_score
                        ELSE 0
                    END
                ), 0)
                FROM match m
                JOIN match_score ms ON m.match_id = ms.match_id
                WHERE (m.home_club_id = ? OR m.away_club_id = ?)
                AND m.status = 'FINISHED'
                """;

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            UUID clubUuid = UUID.fromString(clubId);
            statement.setObject(1, clubUuid, Types.OTHER);
            statement.setObject(2, clubUuid, Types.OTHER);
            statement.setObject(3, clubUuid, Types.OTHER);
            statement.setObject(4, clubUuid, Types.OTHER);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get scored goals", e);
        }
        return 0;
    }

    public Integer getConcededGoals(String clubId) {
        String sql = """
                SELECT COALESCE(SUM(
                    CASE
                        WHEN m.home_club_id = ? THEN ms.away_score
                        WHEN m.away_club_id = ? THEN ms.home_score
                        ELSE 0
                    END
                ), 0)
                FROM match m
                JOIN match_score ms ON m.match_id = ms.match_id
                WHERE (m.home_club_id = ? OR m.away_club_id = ?)
                AND m.status = 'FINISHED'
                """;

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            UUID clubUuid = UUID.fromString(clubId);
            statement.setObject(1, clubUuid, Types.OTHER);
            statement.setObject(2, clubUuid, Types.OTHER);
            statement.setObject(3, clubUuid, Types.OTHER);
            statement.setObject(4, clubUuid, Types.OTHER);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get conceded goals", e);
        }
        return 0;
    }

    public Integer getCleanSheetNumber(String clubId) {
        String sql = """
                SELECT COUNT(*)
                FROM match m
                JOIN match_score ms ON m.match_id = ms.match_id
                WHERE (m.home_club_id = ? AND ms.away_score = 0)
                   OR (m.away_club_id = ? AND ms.home_score = 0)
                AND m.status = 'FINISHED'
                """;

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            UUID clubUuid = UUID.fromString(clubId);
            statement.setObject(1, clubUuid, Types.OTHER);
            statement.setObject(2, clubUuid, Types.OTHER);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get clean sheet number", e);
        }
        return 0;
    }

    public List<Integer> getAllClubsDifferenceGoals() {
        String sql = """
                SELECT c.club_id,
                       SUM(CASE 
                           WHEN m.home_club_id = c.club_id THEN ms.home_score
                           WHEN m.away_club_id = c.club_id THEN ms.away_score
                           ELSE 0
                       END) AS scored_goals,
                       SUM(CASE 
                           WHEN m.home_club_id = c.club_id THEN ms.away_score
                           WHEN m.away_club_id = c.club_id THEN ms.home_score
                           ELSE 0
                       END) AS conceded_goals
                FROM club c
                LEFT JOIN match m ON c.club_id = m.home_club_id OR c.club_id = m.away_club_id
                LEFT JOIN match_score ms ON m.match_id = ms.match_id
                WHERE m.status = 'FINISHED'
                GROUP BY c.club_id
                """;

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            List<Integer> differences = new ArrayList<>();
            while (rs.next()) {
                int scored = rs.getInt("scored_goals");
                int conceded = rs.getInt("conceded_goals");
                differences.add(scored - conceded);
            }
            return differences;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch clubs difference goals", e);
        }
    }
}
