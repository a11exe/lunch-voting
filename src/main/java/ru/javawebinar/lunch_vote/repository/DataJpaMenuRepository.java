package ru.javawebinar.lunch_vote.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.javawebinar.lunch_vote.model.Menu;

import java.util.List;

@Repository
public class DataJpaMenuRepository implements MenuRepository {

    @Autowired
    private CrudMenuRepository crudRepository;

    @Override
    public Menu save(Menu menu) {
        return crudRepository.save(menu);
    }

    @Override
    public boolean delete(int id) {
        return crudRepository.delete(id) != 0;
    }

    @Override
    public Menu get(int id) {
        return crudRepository.findById(id).orElse(null);
    }

    @Override
    public List<Menu> getAll() {
        return crudRepository.findAll();
    }
}
