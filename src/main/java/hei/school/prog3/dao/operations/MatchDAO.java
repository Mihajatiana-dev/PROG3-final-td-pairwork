package hei.school.prog3.dao.operations;

import hei.school.prog3.model.Match;

import java.util.List;

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
    public Match findById(int modelId) {
        return null;
    }
}
