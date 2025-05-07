package hei.school.prog3.dao.operations;

import hei.school.prog3.config.DbConnection;
import hei.school.prog3.model.Transfert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TransfertDAO {

    private final DbConnection dbConnection;

    public Transfert save(Transfert transfert) {
        String sql = """
            INSERT INTO transfert (transfert_id, player_id, club_id, status, transfert_date)
            VALUES (?, ?, ?, ?, ?)
            RETURNING transfert_id, player_id, club_id, status, transfert_date
        """;

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setObject(1, transfert.getId() != null ? UUID.fromString(transfert.getId()) : UUID.randomUUID(), Types.OTHER);
            statement.setObject(2, UUID.fromString(transfert.getPlayerId()), Types.OTHER);
            statement.setObject(3, UUID.fromString(transfert.getClubId()), Types.OTHER);
            statement.setString(4, transfert.getStatus());
            statement.setTimestamp(5, Timestamp.valueOf(transfert.getTransfertDate()));

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapToTransfert(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save transfert", e);
        }
        return null;
    }

    public List<Transfert> findByPlayerId(String playerId) {
        String sql = """
            SELECT transfert_id, player_id, club_id, status, transfert_date
            FROM transfert
            WHERE player_id = ?::uuid
        """;

        List<Transfert> transferts = new ArrayList<>();
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setObject(1, UUID.fromString(playerId), Types.OTHER);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    transferts.add(mapToTransfert(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find transferts by player ID", e);
        }
        return transferts;
    }

    public List<Transfert> findAll() {
        String sql = """
            SELECT transfert_id, player_id, club_id, status, transfert_date
            FROM transfert
        """;

        List<Transfert> transferts = new ArrayList<>();
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                transferts.add(mapToTransfert(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all transferts", e);
        }
        return transferts;
    }

    private Transfert mapToTransfert(ResultSet rs) throws SQLException {
        return new Transfert(
                rs.getObject("transfert_id", UUID.class).toString(),
                rs.getObject("player_id", UUID.class).toString(),
                rs.getObject("club_id", UUID.class).toString(),
                rs.getString("status"),
                rs.getTimestamp("transfert_date").toLocalDateTime()
        );
    }
}
