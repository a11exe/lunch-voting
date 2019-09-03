package ru.alabra.voting.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.alabra.voting.model.Menu;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class DataJpaMenuRepository implements MenuRepository {

    @Autowired
    private CrudMenuRepository crudRepository;

    @Transactional
    public Menu save(Menu menu) {
        return crudRepository.save(menu);
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return crudRepository.delete(id) != 0;
    }

    @Override
    public List<Menu> findByRestaurantId(int restaurant_id) {
        return crudRepository.findByRestaurantId(restaurant_id);
    }

    @Override
    public Optional<Menu> findByRestaurantIdAndId(int restaurant_id, int id) {
        return crudRepository.findByRestaurantIdAndId(restaurant_id, id);
    }

    @Override
    public List<Menu> findByDate(LocalDate date) {
        return crudRepository.findByDate(date);
    }

    @Override
    public List<Menu> findByDateBetween(LocalDate startDate, LocalDate endDate) {
        return crudRepository.findByDateBetween(startDate, endDate);
    }

    @Override
    public List<Menu> findByRestaurantIdAndDate(int restaurant_id, LocalDate date) {
        return crudRepository.findByRestaurantIdAndDate(restaurant_id, date);
    }

    @Override
    public List<Menu> findByRestaurantIdAndDateBetween(int restaurant_id, LocalDate startDate, LocalDate endDate) {
        return crudRepository.findByRestaurantIdAndDateBetween(restaurant_id, startDate, endDate);
    }
    
}
