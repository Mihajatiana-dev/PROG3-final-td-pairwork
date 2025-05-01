package hei.school.prog3.dao.operations;

import hei.school.prog3.config.DbConnection;
import hei.school.prog3.dao.mapper.ClubMapper;
import hei.school.prog3.model.Club;
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
public class ClubDAO implements GenericOperations<Club>{
    private final DbConnection dbConnection;
    private final ClubMapper clubMapper;


    public Club findClubByPlayerId( String playerId) {
        Club clubToFind = new Club();
        String query = "SELECT c.* \n" +
                "FROM club c \n" +
                "JOIN player p ON c.club_id = p.club_id \n" +
                "WHERE p.player_id =?::uuid ";

        try(Connection connection = dbConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
        ){
            preparedStatement.setString(1, playerId);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()) {
                    return clubMapper.apply(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return clubToFind;
    }
    @Override
    public List<Club> showAll(int page, int size) {
        List<Club> clubList = new ArrayList<>();
        String query = "SELECT c.* \n" +
                "FROM club c  LIMIT ? OFFSET ?";

        try(Connection connection = dbConnection.getConnection();
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
        return List.of();
    }
    @Override
    public Club findById(int modelId) {
        return null;
    }
}
