package hei.school.prog3.dao.operations;

import hei.school.prog3.config.DbConnection;
import hei.school.prog3.dto.rest.playerRest.PlayerWithoutClub;
import hei.school.prog3.model.FilterCriteria;
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

public class PlayerDAO implements GenericOperations<Player> {
    private final DbConnection dataSource;

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

    public List<PlayerWithoutClub> saveAll(List<PlayerWithoutClub> players) {
        List<PlayerWithoutClub> savedPlayers = new ArrayList<>();

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
                RETURNING player_id, player_name, number, position, nationality, age
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {
            for (PlayerWithoutClub player : players) {
                pstm.setObject(1, UUID.fromString(player.getId()), Types.OTHER);
                pstm.setString(2, player.getName());
                pstm.setInt(3, player.getNumber());
                pstm.setString(4, player.getPosition().toString());
                pstm.setString(5, player.getNationality());
                pstm.setInt(6, player.getAge());

                try (ResultSet rs = pstm.executeQuery()) {
                    if (rs.next()) {
                        PlayerWithoutClub savedPlayer = new PlayerWithoutClub();
                        savedPlayer.setId(rs.getObject("player_id", UUID.class).toString());
                        savedPlayer.setName(rs.getString("player_name"));
                        savedPlayer.setNumber(rs.getInt("number"));
                        savedPlayer.setPosition(PlayerPosition.valueOf(rs.getString("position")));
                        savedPlayer.setNationality(rs.getString("nationality"));
                        savedPlayer.setAge(rs.getInt("age"));
                        savedPlayers.add(savedPlayer);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save player", e);
        }
        return savedPlayers;
    }
}
