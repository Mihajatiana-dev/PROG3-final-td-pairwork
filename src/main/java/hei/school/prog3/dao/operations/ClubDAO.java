package hei.school.prog3.dao.operations;

import hei.school.prog3.model.Club;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@RequiredArgsConstructor
public class ClubDAO implements GenericOperations<Club>{
    private final CoachDAO coachDAO;

    public Club findClubByPlayerId( String playerID) {
        Club clubToFind = new Club();
        String query = "SELECT c.*\n" +
                "FROM club c\n" +
                "JOIN player p ON c.club_id = p.club_id\n" +
                "WHERE p.player_id =?";

        return null;
    }

    @Override
    public List<Club> showAll(int page, int size) {
        return List.of();
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
