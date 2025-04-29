package hei.school.prog3.dao.operations;

import hei.school.prog3.model.Club;

import java.util.List;

public class ClubDAO implements GenericOperations<Club>{
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
