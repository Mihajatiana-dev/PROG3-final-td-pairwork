package hei.school.prog3.dao.mapper;

import hei.school.prog3.dao.operations.CoachDAO;
import hei.school.prog3.model.Club;
import hei.school.prog3.model.Coach;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.function.Function;

@Component
@RequiredArgsConstructor //when there is attributes insides

public class ClubMapper implements Function<ResultSet, Club> {
    private final CoachDAO coachDAO;

    @Override
    @SneakyThrows
    public Club apply(ResultSet resultSet) {
        Club club = new Club();
        String clubId = resultSet.getString("club_id");
        Coach coach = coachDAO.findCoachByClubId(clubId);

        club.setId(clubId);
        club.setName(resultSet.getString("club_name"));
        club.setAcronym(resultSet.getString("acronym"));
        club.setYearCreation(resultSet.getInt("year_creation"));
        club.setStadium(resultSet.getString("stadium"));
        club.setCoach(coach);

        return  club;
    }
}
