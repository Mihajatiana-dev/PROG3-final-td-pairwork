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
import java.util.List;

@Repository
@RequiredArgsConstructor

public class CoachDAO implements GenericOperations<Coach>{
    private final DbConnection dbConnection;
    private final CoachMapper coachMapper;

    public Coach findCoachByClubId(String clubId) {
        Coach coach = new Coach();
        String query = "SELECT c.* \n" +
                "FROM coach c\n" +
                "JOIN club cl ON c.coach_id = cl.coach_id\n" +
                "WHERE cl.club_id= ?::uuid";
        try(Connection connection = dbConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
        ){
            preparedStatement.setString(1, clubId);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()) {
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
    public List<Coach> save(List<Coach> coaches) {
        return List.of();
    }

    @Override
    public Coach findById(int modelId) {
        return null;
    }
}
