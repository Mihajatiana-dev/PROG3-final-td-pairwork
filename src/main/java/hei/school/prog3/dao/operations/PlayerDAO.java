package hei.school.prog3.dao.operations;

import hei.school.prog3.config.DbConnection;
import hei.school.prog3.dao.mapper.PlayerMapper;
import hei.school.prog3.dto.Other.FilterCriteria;
import hei.school.prog3.dto.PlayerRest.PlayerStatistics;
import hei.school.prog3.model.*;
import hei.school.prog3.model.enums.DurationUnit;
import hei.school.prog3.model.enums.PlayerPosition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor

public class PlayerDAO implements GenericOperations<Player> {

    private final DbConnection dataSource;
    private final PlayerMapper playerMapper;

    public List<Player> getAllFilteredPlayer(List<FilterCriteria> filterCriteriaList, int page, int size) {
        if (page < 1) {
            throw new IllegalArgumentException("Page must be greater than 0 but actual is " + page);
        }
        List<Player> filteredPlayers = new ArrayList<>();

        String sql = "SELECT p.player_id, p.player_name, p.number, p.position, p.nationality, p.age, " +
                "c.club_id, c.club_name, c.acronym, c.year_creation, c.stadium, " +
                "co.coach_name, co.nationality " +
                "FROM player p " +
                "LEFT JOIN club c ON p.club_id = c.club_id " +
                "LEFT JOIN coach co ON c.coach_id = co.coach_id " +
                "WHERE 1=1";

        List<String> conditions = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        for (FilterCriteria filterCriteria : filterCriteriaList) {
            String column = filterCriteria.getColumn();
            Object value = filterCriteria.getValue();

            if ("name".equals(column)) {
                conditions.add("p.player_name ILIKE ?");
                values.add("%" + value + "%");
            } else if ("ageMinimum".equals(column)) {
                conditions.add("p.age >= ?");
                values.add(value);
            } else if ("ageMaximum".equals(column)) {
                conditions.add("p.age <= ?");
                values.add(value);
            } else if ("clubName".equals(column)) {
                conditions.add("c.club_name ILIKE ?");
                values.add("%" + value + "%");
            }
        }

        if (!conditions.isEmpty()) {
            sql += " AND " + String.join(" AND ", conditions);
        }
        sql += " LIMIT ? OFFSET ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            int index = 1;
            for (Object value : values) {
                statement.setObject(index++, value);
            }
            statement.setInt(index++, size);
            statement.setInt(index, size * (page - 1));

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Player player = playerMapper.apply(resultSet);
                    filteredPlayers.add(player);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return filteredPlayers;
    }

    @Override
    public List<Player> showAll(int page, int size) {
        return List.of();
    }

    @Override
    public List<Player> save(List<Player> players) {
        return List.of();
    }

    @Override
    public Player findById(String Id) {
        String checkPlayerSql = """
                SELECT *
                FROM player
                WHERE player_id = ?""";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(checkPlayerSql)) {
            preparedStatement.setObject(1, UUID.fromString(Id), Types.OTHER);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return playerMapper.apply(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void attachPlayerToClub(String playerId, String clubId) {
        String sql = """
                UPDATE player 
                SET club_id = ?::uuid 
                WHERE player_id = ?::uuid
                AND (club_id IS NULL OR club_id = ?::uuid)
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setString(1, clubId);
            pstm.setString(2, playerId);
            pstm.setString(3, clubId);

            int affectedRows = pstm.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException("Player is already in another club");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to attach player to club", e);
        }
    }

    public List<Player> savePLayerWithoutUpdate(List<Player> players) {
        List<Player> savedPlayers = new ArrayList<>();

        String sql = """
                INSERT INTO player (player_id, player_name, number, position, nationality, age, club_id)
                VALUES (?, ?, ?, ?::position_enum, ?, ?, ?::uuid)
                RETURNING player_id, player_name, number, position, nationality, age, club_id
                """;

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                for (Player player : players) {
                    pstm.setObject(1, UUID.fromString(player.getId()), Types.OTHER);
                    pstm.setString(2, player.getName());
                    pstm.setInt(3, player.getNumber());
                    pstm.setString(4, player.getPosition().toString());
                    pstm.setString(5, player.getNationality());
                    pstm.setInt(6, player.getAge());
                    pstm.setString(7, player.getClub().getId());

                    try (ResultSet rs = pstm.executeQuery()) {
                        if (rs.next()) {
                            Player savedPlayer = new Player();
                            savedPlayer.setId(rs.getString("player_id"));
                            savedPlayer.setName(rs.getString("player_name"));
                            savedPlayer.setNumber(rs.getInt("number"));
                            savedPlayer.setPosition(PlayerPosition.valueOf(rs.getString("position")));
                            savedPlayer.setNationality(rs.getString("nationality"));
                            savedPlayer.setAge(rs.getInt("age"));
                            savedPlayer.setClub(new Club(rs.getString("club_id")));
                            savedPlayers.add(savedPlayer);
                        }
                    }
                    pstm.clearParameters();
                }
                connection.commit();
            } catch (RuntimeException e) {
                connection.rollback();
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save player", e);
        }
        return savedPlayers;
    }

    public List<Player> saveAll(List<Player> players) {
        List<Player> savedPlayers = new ArrayList<>();

        String sql = """
                INSERT INTO player (player_id, player_name, number, position, nationality, age)
                VALUES (?, ?, ?, ?::position_enum, ?, ?)
                ON CONFLICT (player_id)
                DO UPDATE SET
                    player_name = EXCLUDED.player_name,
                    number = EXCLUDED.number,
                    position = EXCLUDED.position,
                    nationality = EXCLUDED.nationality,
                    age = EXCLUDED.age
                RETURNING player_id, player_name, number, position, nationality, age, club_id
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {
            for (Player player : players) {
                pstm.setObject(1, UUID.fromString(player.getId()), Types.OTHER);
                pstm.setString(2, player.getName());
                pstm.setInt(3, player.getNumber());
                pstm.setString(4, player.getPosition().toString());
                pstm.setString(5, player.getNationality());
                pstm.setInt(6, player.getAge());

                try (ResultSet rs = pstm.executeQuery()) {
                    if (rs.next()) {
                        Player savedPlayer = new Player();
                        savedPlayer.setId(rs.getObject("player_id", UUID.class).toString());
                        savedPlayer.setName(rs.getString("player_name"));
                        savedPlayer.setNumber(rs.getInt("number"));
                        savedPlayer.setPosition(PlayerPosition.valueOf(rs.getString("position")));
                        savedPlayer.setNationality(rs.getString("nationality"));
                        savedPlayer.setAge(rs.getInt("age"));

                        Club club = new Club(rs.getString("club_id"));
                        savedPlayer.setClub(club);

                        savedPlayers.add(savedPlayer);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save player", e);
        }
        return savedPlayers;
    }

    public Integer getScoredGoals(UUID playerId, int seasonYear) {
        String sql = """
                SELECT COUNT(*)
                FROM goal g
                JOIN match m ON g.match_id = m.match_id
                JOIN season s ON m.season_id = s.season_id
                WHERE g.player_id = ?
                AND s.year = ?
                AND g.own_goal = false
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setObject(1, playerId, Types.OTHER);
            statement.setInt(2, seasonYear);

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

    public PlayingTime getPlayingTime(UUID playerId, int seasonYear) {
        String sql = """
                SELECT value, duration_unit
                FROM playing_time pt
                JOIN season s ON pt.season_id = s.season_id
                WHERE pt.player_id = ?
                AND s.year = ?
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setObject(1, playerId, Types.OTHER);
            statement.setInt(2, seasonYear);

            try (ResultSet rs = statement.executeQuery()) {
                PlayingTime playingTime = new PlayingTime();
                if (rs.next()) {
                    playingTime.setValue(rs.getDouble("value"));
                    playingTime.setDurationUnit(DurationUnit.valueOf(rs.getString("duration_unit")));
                } else {
                    playingTime.setValue(0);
                    playingTime.setDurationUnit(DurationUnit.MINUTE);
                }
                return playingTime;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get playing time", e);
        }
    }

    public List<Player> findAllPlayers() {
        String sql = """
                SELECT p.player_id, p.player_name, p.number, p.position, p.nationality, p.age,
                       c.club_id, c.club_name, c.acronym, c.year_creation, c.stadium
                FROM player p
                LEFT JOIN club c ON p.club_id = c.club_id
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            List<Player> players = new ArrayList<>();
            while (rs.next()) {
                Player player = new Player(
                        rs.getString("player_id"),
                        rs.getString("player_name"),
                        rs.getInt("number"),
                        PlayerPosition.valueOf(rs.getString("position")),
                        rs.getString("nationality"),
                        rs.getInt("age")
                );

                if (rs.getString("club_id") != null) {
                    Club club = new Club(
                            rs.getString("club_id"),
                            rs.getString("club_name"),
                            rs.getString("acronym"),
                            rs.getInt("year_creation"),
                            rs.getString("stadium")
                    );
                    player.setClub(club);
                }

                players.add(player);
            }
            return players;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch players", e);
        }
    }

    public Integer getScoredGoals(String playerId) {
        String sql = """
                SELECT COUNT(*)
                FROM goal g
                WHERE g.player_id = ?
                AND g.own_goal = false
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setObject(1, UUID.fromString(playerId), Types.OTHER);

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

    public PlayingTime getTotalPlayingTime(String playerId) {
        String sql = """
                SELECT SUM(value), duration_unit
                FROM playing_time
                WHERE player_id = ?
                GROUP BY duration_unit
                """;

        double totalMinutes = 0;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setObject(1, UUID.fromString(playerId), Types.OTHER);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    double value = rs.getDouble(1);
                    String unit = rs.getString(2);

                    // convert value to minute
                    switch (DurationUnit.valueOf(unit)) {
                        case SECOND -> totalMinutes += value / 60;
                        case MINUTE -> totalMinutes += value;
                        case HOUR -> totalMinutes += value * 60;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get playing time", e);
        }

        PlayingTime playingTime = new PlayingTime();
        playingTime.setValue(totalMinutes);
        playingTime.setDurationUnit(DurationUnit.MINUTE);
        return playingTime;
    }
}
