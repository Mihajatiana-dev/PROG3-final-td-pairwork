package hei.school.prog3.dao.operations;

import hei.school.prog3.config.DbConnection;
import hei.school.prog3.dao.mapper.ClubMapper;
import hei.school.prog3.model.Club;
import hei.school.prog3.model.Coach;
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
public class ClubDAO implements GenericOperations<Club>{
    private final DbConnection dbConnection;
    private final ClubMapper clubMapper;
    private final CoachDAO coachDAO;

    public List<Player> findClubPlayer(String Id, int page, int size){
        List<Player> playerList = new ArrayList<>();
        String query = "SELECT * FROM player WHERE club_id=?::uuid ";

        try(Connection connection = dbConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


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
    public List<Club> save(List<Club> clubToSave) {
        List<Club> clubList = new ArrayList<>();

        String clubQuery = "INSERT INTO club (club_id, club_name, acronym, year_creation, stadium, coach_id) " +
                "VALUES (?::uuid, ?, ?, ?, ?, ?::uuid) " +
                "ON CONFLICT (club_id) DO UPDATE SET club_name = EXCLUDED.club_name, acronym = EXCLUDED.acronym, " +
                "year_creation = EXCLUDED.year_creation, stadium = EXCLUDED.stadium, coach_id = EXCLUDED.coach_id";

        try (Connection connection = dbConnection.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(clubQuery)) {
                for (Club club : clubToSave) {
                    Coach coach = club.getCoach();
                    //create UUID IF NULL
                    if (coach.getId() == null || coach.getId().isEmpty()) {
                        coach.setId(java.util.UUID.randomUUID().toString());
                    }
                    coachDAO.save(List.of(coach));

                    preparedStatement.setString(1, club.getId());
                    preparedStatement.setString(2, club.getName());
                    preparedStatement.setString(3, club.getAcronym());
                    preparedStatement.setInt(4, club.getYearCreation());
                    preparedStatement.setString(5, club.getStadium());
                    preparedStatement.setString(6, coach.getId());
                    preparedStatement.addBatch();
                }

                preparedStatement.executeBatch();
                connection.commit();
                clubList.addAll(clubToSave);

            } catch (RuntimeException e) {
                connection.rollback();
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return clubList;
    }
    @Override
    public Club findById(int modelId) {
        return null;
    }
}
