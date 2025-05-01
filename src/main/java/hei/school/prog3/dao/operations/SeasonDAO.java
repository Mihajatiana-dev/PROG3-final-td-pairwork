package hei.school.prog3.dao.operations;

import hei.school.prog3.model.Season;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@RequiredArgsConstructor
public class SeasonDAO implements GenericOperations<Season>{
    @Override
    public List<Season> showAll(int page, int size) {
        return List.of();
    }

    @Override
    public List<Season> save(List<Season> seasons) {
        return List.of();
    }

    @Override
    public Season findById(int modelId) {
        return null;
    }
}
