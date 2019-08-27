package ru.javawebinar.graduation;

import ru.javawebinar.graduation.model.Role;
import ru.javawebinar.graduation.model.User;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.graduation.model.AbstractBaseEntity.START_SEQ;

public class UserTestData {
    public static final int ADMIN_ID = START_SEQ;
    public static final int USER_ID = START_SEQ + 1;
    public static final int USER_ID2 = START_SEQ + 2;
    public static final int USER_ID3 = START_SEQ + 3;

    public static final User USER = new User(USER_ID, "Alex", "alex@yandex.ru", "password", Role.ROLE_USER);
    public static final User USER2 = new User(USER_ID2, "Bob", "bob@yandex.ru", "password", Role.ROLE_USER);
    public static final User USER3 = new User(USER_ID3, "Jhon", "jhon@yandex.ru", "password", Role.ROLE_USER);
    public static final User ADMIN = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", Role.ROLE_ADMIN);

    public static void assertMatch(User actual, User expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "registered", "roles");
    }

    public static void assertMatch(Iterable<User> actual, User... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<User> actual, Iterable<User> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("registered", "roles").isEqualTo(expected);
    }
}
