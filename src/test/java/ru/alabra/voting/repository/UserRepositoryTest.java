package ru.alabra.voting.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import ru.alabra.voting.model.Role;
import ru.alabra.voting.model.User;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static ru.alabra.voting.TestData.*;

/**
 * @author Alexander Abramov (alllexe@mail.ru)
 * @version 1
 * @since 27.08.2019
 */
class UserRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    protected CrudUserRepository repository;

    @Test
    void create() throws Exception {
        User newUser = new User(null, "New", "new@gmail.com", "newPass", true, LocalDate.now(), Collections.singleton(Role.ROLE_USER));
        User created = repository.save(new User(newUser));
        newUser.setId(created.getId());
        assertMatchIgnoringFields(new String[]{"registered", "password"}, created, newUser);
        assertMatchIgnoringFields(new String[]{"registered", "password"}, repository.findAll(), ADMIN, USER, USER2, USER3, newUser);
    }

    @Test
    void delete() throws Exception {
        repository.delete(USER_ID);
        assertMatchIgnoringFields(new String[]{"registered", "password"}, repository.findAll(), ADMIN, USER2, USER3);
    }

    @Test
    void get() throws Exception {
        User user = repository.findById(ADMIN_ID).orElse(null);
        assertMatchIgnoringFields(new String[]{"registered", "password"}, user, ADMIN);
    }

    @Test
    void getAll() throws Exception {
        List<User> all = repository.findAll();
        assertMatchIgnoringFields(new String[]{"registered", "password"}, all, ADMIN, USER, USER2, USER3);
    }

    @Test
    void update() throws Exception {
        User updated = new User(USER);
        updated.setName("UpdatedName");
        updated.setRoles(Collections.singletonList(Role.ROLE_ADMIN));
        repository.save(new User(updated));
        assertMatchIgnoringFields(new String[]{"registered"}, repository.findById(USER_ID).orElse(null), updated);
    }
}