package ru.javawebinar.graduation.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.graduation.model.Restaraunt;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public class DataJpaRestarauntRepository implements RestarauntRepository {

    @Autowired
    private CrudRestarauntrRepository crudRepository;

    @Override
    public Restaraunt save(Restaraunt restaraunt) {
        return crudRepository.save(restaraunt);
    }

    @Override
    public boolean delete(int id) {
        return crudRepository.delete(id) != 0;
    }

    @Override
    public Restaraunt get(int id) {
        return crudRepository.findById(id).orElse(null);
    }

    @Override
    public List<Restaraunt> getAll() {
        return crudRepository.findAll();
    }
}
