package hei.school.prog3.dao.operations;

import hei.school.prog3.model.Player;

import java.util.List;

public class PlayerDAO implements GenericOperations<Player>{
    @Override
    public List<Player> showAll(int page, int size) {
        return List.of();
    }

    @Override
    public List<Player> save(List<Player> players) {
        return List.of();
    }

    @Override
    public Player findById(int modelId) {
        return null;
    }
}
