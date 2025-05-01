package hei.school.prog3.dao.mapper;

import hei.school.prog3.dao.operations.CoachDAO;
import hei.school.prog3.model.Club;
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
        return  null;
    }
}
