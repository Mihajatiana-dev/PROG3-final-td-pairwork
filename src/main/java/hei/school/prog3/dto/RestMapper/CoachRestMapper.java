package hei.school.prog3.dto.RestMapper;

import hei.school.prog3.dto.CoachRest.CoachSimpleRequest;
import hei.school.prog3.dto.CoachRest.CoachResponse;
import hei.school.prog3.model.Coach;
import org.springframework.stereotype.Component;

@Component

public class CoachRestMapper {
    public CoachResponse toRest(Coach coach) {
        return  new CoachResponse(
                coach.getName(),
                coach.getNationality()
        );
    }

    public Coach toModel(CoachSimpleRequest coachSimpleRequest) {
        if (coachSimpleRequest == null) {
            return null;
        }
        Coach coach = new Coach();
        coach.setName(coachSimpleRequest.getName());
        coach.setNationality(coachSimpleRequest.getNationality());
        return coach;
    }
}
