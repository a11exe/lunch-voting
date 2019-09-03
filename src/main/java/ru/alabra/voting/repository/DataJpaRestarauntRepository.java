package ru.alabra.voting.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.alabra.voting.model.Restaurant;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class DataJpaRestarauntRepository implements RestarauntRepository {

    @Autowired
    private CrudRestarauntrRepository crudRepository;

    @Override
    @Transactional
    public Restaurant save(Restaurant restaurant) {
        return crudRepository.save(restaurant);
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return crudRepository.delete(id) != 0;
    }

    @Override
    public Optional<Restaurant> findById(int id) {
        return crudRepository.findById(id);
    }

    @Override
    public List<Restaurant> getAll() {
        return crudRepository.findAll();
    }
}
