package hei.school.prog3.dao.operations;

import hei.school.prog3.config.DbConnection;
import hei.school.prog3.dao.mapper.PlayerMapper;
import hei.school.prog3.model.FilterCriteria;
import hei.school.prog3.model.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@Repository
@RequiredArgsConstructor

public class PlayerDAO implements GenericOperations<Player>{

    private final DbConnection dbConnection;
    private final PlayerMapper playerMapper;

    public List<Player> getAllFilteredPlayer(List<FilterCriteria> filterCriteriaList, int page, int size) {
        if (page < 1) {
            throw new IllegalArgumentException("Page must be greater than 0 but actual is " + page);
        }
        List<Player> filteredPlayers = new ArrayList<>();

        String sql = "SELECT p.player_id, p.player_name, p.number, p.position, p.nationality, p.age, " +
                "c.club_id, c.club_name, c.acronym, c.year_creation, c.stadium, " +
                "co.coach_name, co.nationality" +
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

        try (Connection connection = dbConnection.getConnection();
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
    public Player findById(int modelId) {
        return null;
    }

    public List<Player> getFilteredPlayersByNameOrAge(List<FilterCriteria> filterCriteria, int page, int size) {
        return null;
    }
}
