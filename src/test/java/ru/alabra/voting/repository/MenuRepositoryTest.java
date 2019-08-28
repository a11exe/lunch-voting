package ru.alabra.voting.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.alabra.voting.model.Menu;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static ru.alabra.voting.MenuTestData.*;
import static ru.alabra.voting.RestarauntTestData.MC;


/**
 * @author Alexander Abramov (alllexe@mail.ru)
 * @version 1
 * @since 28.08.2019
 */
@SpringJUnitConfig(locations = {
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
class MenuRepositoryTest {

    @Autowired
    protected MenuRepository repository;

    @Test
    void create() throws Exception {
        Menu newMenu = new Menu(null, new Timestamp(System.currentTimeMillis()), "burger 150; coffe 250; potato 80", MC);
        Menu created = repository.save(newMenu);
        created.setId(created.getId());
        assertMatch(created, newMenu);
        assertMatch(repository.getAll(), M1, M2, M3, M4, newMenu);
    }

    @Test
    void delete() throws Exception {
        repository.delete(M1_ID);
        assertMatch(repository.getAll(), M2, M3, M4);
    }

    @Test
    void get() throws Exception {
        Menu menu = repository.get(M1_ID);
        assertMatch(menu, M1);
    }

    @Test
    void getAll() throws Exception {
        List<Menu> all = repository.getAll();
        assertMatch(all, M1, M2, M3, M4);
    }

    @Test
    void update() throws Exception {
        Menu updated = new Menu(M1);
        updated.setDescription("Updated description");
        repository.save(new Menu(updated));
        assertMatch(repository.get(M1_ID), updated);
    }

}