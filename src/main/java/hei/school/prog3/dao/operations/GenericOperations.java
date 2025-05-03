package hei.school.prog3.dao.operations;

import java.util.List;

public interface GenericOperations <Model> {
    List<Model> showAll(int page, int size);
    List<Model> save(List<Model> models);
    Model findById(String modelId);
}
