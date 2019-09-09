package ru.alabra.voting.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
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
    protected CrudRestarauntRepository repository;

    @Test
    void create() throws Exception {
        Restaurant newRest = new Restaurant(null, "NewRest", "Super meat");
        Restaurant created = repository.save(newRest);
        created.setId(created.getId());
        assertMatch(created, newRest);
        assertMatch(repository.findAll(), MC, KFC, BK, IL, newRest);
    }

    @Test
    void delete() throws Exception {
        repository.delete(MC_ID);
        assertMatch(repository.findAll(), KFC, BK, IL);
    }

    @Test
    void get() throws Exception {
        Restaurant restaurant = repository.findById(MC_ID).orElse(null);
        assertMatch(restaurant, MC);
    }

    @Test
    void getAll() throws Exception {
        List<Restaurant> all = repository.findAll();
        assertMatch(all, MC, KFC, BK, IL);
    }

    @Test
    void update() throws Exception {
        Restaurant updated = new Restaurant(MC);
        updated.setName("UpdatedName");
        repository.save(new Restaurant(updated));
        assertMatch(repository.findById(MC_ID).orElse(null), updated);
    }

}