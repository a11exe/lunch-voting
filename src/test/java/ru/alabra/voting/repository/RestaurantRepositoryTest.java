package ru.alabra.voting.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.alabra.voting.model.Restaurant;

import java.util.List;

import static ru.alabra.voting.TestData.*;


/**
 * @author Alexander Abramov (alllexe@mail.ru)
 * @version 1
 * @since 28.08.2019
 */
class RestaurantRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    protected CrudRestaurantRepository repository;

    @Test
    void create() {
        Restaurant newRest = new Restaurant(null, "NewRest", "Super meat");
        Restaurant created = repository.save(newRest);
        created.setId(created.getId());
        assertMatch(created, newRest);
        assertMatch(repository.findAll(), MC, KFC, BK, IL, newRest);
    }

    @Test
    void delete() {
        repository.deleteById(MC_ID);
        assertMatch(repository.findAll(), KFC, BK, IL);
    }

    @Test
    void findById() {
        Restaurant restaurant = repository.findById(MC_ID).orElse(null);
        assertMatch(restaurant, MC);
    }

    @Test
    void findAll() {
        List<Restaurant> all = repository.findAll();
        assertMatch(all, MC, KFC, BK, IL);
    }

    @Test
    void update() {
        Restaurant updated = new Restaurant(MC);
        updated.setName("UpdatedName");
        repository.save(new Restaurant(updated));
        assertMatch(repository.findById(MC_ID).orElse(null), updated);
    }

}