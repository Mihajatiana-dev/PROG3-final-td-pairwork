package hei.school.prog3.dao.operations;

import hei.school.prog3.model.Match;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@RequiredArgsConstructor
public class MatchDAO implements GenericOperations<Match>{
    @Override
    public List<Match> showAll(int page, int size) {
        return List.of();
    }

    @Override
    public List<Match> save(List<Match> matches) {
        return List.of();
    }

    @Override
    public Match findById(String modelId) {
        return null;
    }
}
