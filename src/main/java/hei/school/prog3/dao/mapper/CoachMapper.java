package hei.school.prog3.dao.mapper;

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

public class CoachMapper implements Function<ResultSet, Coach> {
    @Override
    @SneakyThrows
    public Coach apply(ResultSet resultSet) {
        Coach coach = new Coach();
        coach.setId(resultSet.getString("coach_id"));
        coach.setName(resultSet.getString("coach_name"));
        coach.setNationality(resultSet.getString("nationality"));
        return coach;
    }
}
