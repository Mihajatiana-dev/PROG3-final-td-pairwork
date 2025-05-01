package hei.school.prog3.api.RestMapper;

import hei.school.prog3.api.dto.response.ClubResponse;
import hei.school.prog3.api.dto.response.CoachResponse;
import hei.school.prog3.model.Club;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClubRestMapper  {
    @Autowired private CoachRestMapper coachRestMapper;

    public ClubResponse toRest(Club club) {
        CoachResponse coachResponse = coachRestMapper.apply(club.getCoach());
        return new ClubResponse(
                club.getId(),
                club.getName(),
                club.getAcronym(),
                club.getYearCreation(),
                club.getStadium(),
                coachResponse
        );
    }
}