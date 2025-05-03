package hei.school.prog3.dao.operations;

import hei.school.prog3.config.DbConnection;
import hei.school.prog3.dao.mapper.CoachMapper;
import hei.school.prog3.model.Coach;
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

public class CoachDAO implements GenericOperations<Coach> {
    private final DbConnection dbConnection;
    private final CoachMapper coachMapper;

    public Coach findCoachByClubId(String clubId) {
        Coach coach = new Coach();
        String query = "SELECT c.* \n" +
                "FROM coach c\n" +
                "JOIN club cl ON c.coach_id = cl.coach_id\n" +
                "WHERE cl.club_id= ?::uuid";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.setString(1, clubId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return coachMapper.apply(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return coach;
    }

    @Override
    public List<Coach> showAll(int page, int size) {
        return List.of();
    }

    @Override
    public List<Coach> save(List<Coach> coachToSave) {
        List<Coach> coachList = new ArrayList<>();
        String coachQuery = "INSERT INTO coach (coach_id, coach_name    , nationality) " +
                "VALUES (?::uuid, ?, ?) " +
                "ON CONFLICT (coach_id) DO UPDATE SET coach_name = EXCLUDED.coach_name, nationality = EXCLUDED.nationality";

        try (Connection connection = dbConnection.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(coachQuery)) {
                for (Coach coach : coachToSave) {
                    preparedStatement.setString(1, coach.getId());
                    preparedStatement.setString(2, coach.getName());
                    preparedStatement.setString(3, coach.getNationality());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
                connection.commit();
                coachList.addAll(coachToSave);
            } catch (RuntimeException e) {
                connection.rollback();
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return coachList;
    }

    @Override
    public Coach findById(String modelId) {
        return null;
    }

    public Coach findByName(String name) {
        String sql = "select coach_id, coach_name, nationality from coach where coach_name = ?";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setString(1, name);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    Coach coach = new Coach();
                    coach.setId(rs.getString("coach_id"));
                    coach.setName(rs.getString("coach_name"));
                    coach.setNationality(rs.getString("nationality"));
                    return coach;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find coach by name", e);
        }
        return null;
    }
}
