package hei.school.prog3.dao.operations;

import hei.school.prog3.model.FilterCriteria;
import hei.school.prog3.model.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@RequiredArgsConstructor

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

    public List<Player> getFilteredPlayersByNameOrAge(List<FilterCriteria> filterCriteria, int page, int size) {
        return null;
    }
}
