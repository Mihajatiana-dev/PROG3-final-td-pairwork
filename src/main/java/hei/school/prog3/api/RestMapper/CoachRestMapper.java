package hei.school.prog3.api.RestMapper;

import hei.school.prog3.api.dto.request.CoachSimpleRequest;
import hei.school.prog3.api.dto.response.CoachResponse;
import hei.school.prog3.model.Coach;
import org.springframework.stereotype.Component;

import java.util.function.Function;
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
