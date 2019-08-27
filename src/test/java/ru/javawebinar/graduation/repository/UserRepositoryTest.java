package ru.javawebinar.graduation.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.javawebinar.graduation.model.Role;
import ru.javawebinar.graduation.model.User;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static ru.javawebinar.graduation.UserTestData.*;

/**
 * @author Alexander Abramov (alllexe@mail.ru)
 * @version 1
 * @since 27.08.2019
 */
@SpringJUnitConfig(locations = {
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
class UserRepositoryTest {

    @Autowired
    protected UserRepository repository;

    @Test
    void create() throws Exception {
        User newUser = new User(null, "New", "new@gmail.com", "newPass", true, new Date(), Collections.singleton(Role.ROLE_USER));
        User created = repository.save(new User(newUser));
        newUser.setId(created.getId());
        assertMatch(created, newUser);
        assertMatch(repository.getAll(), ADMIN, USER, USER2, USER3, newUser);
    }

    @Test
    void delete() throws Exception {
        repository.delete(USER_ID);
        assertMatch(repository.getAll(), ADMIN, USER2, USER3);
    }

    @Test
    void get() throws Exception {
        User user = repository.get(ADMIN_ID);
        assertMatch(user, ADMIN);
    }

    @Test
    void getAll() throws Exception {
        List<User> all = repository.getAll();
        assertMatch(all, ADMIN, USER, USER2, USER3);
    }


    @Test
    void update() throws Exception {
        User updated = new User(USER);
        updated.setName("UpdatedName");
        updated.setRoles(Collections.singletonList(Role.ROLE_ADMIN));
        repository.save(new User(updated));
        assertMatch(repository.get(USER_ID), updated);
    }
}