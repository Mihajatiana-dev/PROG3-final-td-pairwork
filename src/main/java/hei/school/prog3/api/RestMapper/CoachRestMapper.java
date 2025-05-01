package hei.school.prog3.api.RestMapper;

import hei.school.prog3.api.dto.response.CoachResponse;
import hei.school.prog3.model.Coach;
import org.springframework.stereotype.Component;

import java.util.function.Function;
@Component

public class CoachRestMapper implements Function<Coach, CoachResponse> {
    @Override
    public CoachResponse apply(Coach coach) {
        return  new CoachResponse(
                coach.getName(),
                coach.getNationality()
        );
    }
}
