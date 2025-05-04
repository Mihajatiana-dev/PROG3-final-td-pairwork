package hei.school.prog3.dao.operations;

import hei.school.prog3.api.dto.request.ClubSimpleRequest;
import hei.school.prog3.api.dto.request.CoachSimpleRequest;
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


    public List<Club> saveAll(List<ClubSimpleRequest> clubToSave) {
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

            for (ClubSimpleRequest request : clubToSave) {
                // manage the coach first
                CoachSimpleRequest coachRequest = request.getCoach();
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

        String upsertPlayerSql = """
                INSERT INTO player (player_id, player_name, number, position, nationality, age, club_id)
                VALUES (?, ?, ?, ?::position_enum, ?, ?, ?)
                ON CONFLICT (player_id)
                DO UPDATE SET
                    player_name = EXCLUDED.player_name,
                    number = EXCLUDED.number,
                    position = EXCLUDED.position,
                    nationality = EXCLUDED.nationality,
                    age = EXCLUDED.age,
                    club_id = EXCLUDED.club_id
                RETURNING player_id, player_name, number, position, nationality, age
                """;

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement clearStmt = connection.prepareStatement(clearExistingPlayersSql);
             PreparedStatement upsertStmt = connection.prepareStatement(upsertPlayerSql)) {

            connection.setAutoCommit(false);

            // detach club_id from existing players
            clearStmt.setObject(1, UUID.fromString(clubId), Types.OTHER);
            clearStmt.executeUpdate();

            // upsert new players
            List<Player> result = new ArrayList<>();

            for (Player player : newPlayers) {
                upsertStmt.setObject(1, player.getId() != null ?
                        UUID.fromString(player.getId()) : UUID.randomUUID(), Types.OTHER);
                upsertStmt.setString(2, player.getName());
                upsertStmt.setInt(3, player.getNumber());
                upsertStmt.setString(4, player.getPosition().name());
                upsertStmt.setString(5, player.getNationality());
                upsertStmt.setInt(6, player.getAge());
                upsertStmt.setObject(7, UUID.fromString(clubId), Types.OTHER);

                try (ResultSet rs = upsertStmt.executeQuery()) {
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
                upsertStmt.clearParameters();
            }

            connection.commit();
            return result;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to replace club players", e);
        }
    }
}
