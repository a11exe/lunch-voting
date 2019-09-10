package ru.alabra.voting.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.alabra.voting.model.Menu;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static ru.alabra.voting.TestData.*;


/**
 * @author Alexander Abramov (alllexe@mail.ru)
 * @version 1
 * @since 28.08.2019
 */
class MenuRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    protected CrudMenuRepository repository;

    @Test
    void create() throws Exception {
        LocalDate today = LocalDate.now();
        Menu newMenu = new Menu(null, today, "burger 150; coffe 250; potato 80", MC);
        Menu created = repository.save(newMenu);
        created.setId(created.getId());
        assertMatch(created, newMenu);
        assertMatch(repository.findByDate(today), List.of(newMenu));
    }

    @Test
    void delete() throws Exception {
        repository.delete(M1_ID);
        assertMatch(repository.findByDate(M1.getDate()), M2, M3);
    }

    @Test
    void findByRestaurantIdAndId() throws Exception {
        Menu menu = repository.findByRestaurantIdAndId(MC_ID, M1_ID).orElse(null);
        assertMatch(menu, M1);
    }

    @Test
    void findByDate() throws Exception {
        List<Menu> all = repository.findByDate(M1.getDate());
        assertMatch(all, M1, M2, M3);
    }

    @Test
    void findByDateBetween() throws Exception {
        List<Menu> all = repository.findByDateBetween(M1.getDate(), M4.getDate());
        assertMatch(all, M1, M2, M3, M4);
    }

    @Test
    void findByRestaurantIdAndDate() throws Exception {
        List<Menu> all = repository.findByRestaurantIdAndDate(MC_ID, M1.getDate());
        assertMatch(all, List.of(M1));
    }

    @Test
    void findByRestaurantIdAndDateBetween() throws Exception {
        List<Menu> all = repository.findByRestaurantIdAndDateBetween(MC_ID, M4.getDate(), M4.getDate());
        assertMatch(all, Collections.emptyList());
    }

    @Test
    void findByRestaurantId() throws Exception {
        List<Menu> all = repository.findByRestaurantId(MC_ID);
        assertMatch(all, List.of(M1));
    }

    @Test
    void update() throws Exception {
        Menu updated = new Menu(M1);
        updated.setDescription("Updated description");
        repository.save(updated);
        assertMatch(repository.findByRestaurantIdAndId(MC_ID, M1_ID).orElse(null), updated);
    }

}