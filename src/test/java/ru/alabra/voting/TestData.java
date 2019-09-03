package ru.alabra.voting;

import ru.alabra.voting.model.*;
import ru.alabra.voting.web.json.JsonUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.alabra.voting.model.AbstractBaseEntity.START_SEQ;

/**
 * @author Alexander Abramov (alllexe@mail.ru)
 * @version 1
 * @since 30.08.2019
 */
public class TestData {

    public static final int ADMIN_ID = START_SEQ;
    public static final int USER_ID = START_SEQ + 1;
    public static final int USER_ID2 = START_SEQ + 2;
    public static final int USER_ID3 = START_SEQ + 3;

    public static final User USER = new User(USER_ID, "Alex", "alex@yandex.ru", "password", Role.ROLE_USER);
    public static final User USER2 = new User(USER_ID2, "Bob", "bob@yandex.ru", "password", Role.ROLE_USER);
    public static final User USER3 = new User(USER_ID3, "Jhon", "jhon@yandex.ru", "password", Role.ROLE_USER);
    public static final User ADMIN = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", Role.ROLE_ADMIN);

    public static final int MC_ID = START_SEQ + 4;
    public static final int KFC_ID = START_SEQ + 5;
    public static final int BK_ID = START_SEQ + 6;
    public static final int IL_ID = START_SEQ + 7;

    public static final Restaurant MC = new Restaurant(MC_ID, "MCDonalds", "mc ddd");
    public static final Restaurant KFC = new Restaurant(KFC_ID, "KFC", "chicken");
    public static final Restaurant BK = new Restaurant(BK_ID, "Burger King", "big burgers");
    public static final Restaurant IL = new Restaurant(IL_ID, "Il Patio", "pizza");

    public static final int M1_ID = START_SEQ + 8;
    public static final int M2_ID = START_SEQ + 9;
    public static final int M3_ID = START_SEQ + 10;
    public static final int M4_ID = START_SEQ + 11;

    private static String str = "2015-05-30";
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static LocalDate dateTime = LocalDate.parse(str, formatter);

    public static Menu M1 = new Menu(M1_ID, dateTime, "burger 100; coffe 200; potato 50", MC);;
    public static Menu M2 = new Menu(M2_ID, dateTime, "chiken roll 100; black burger 200; potato 50", KFC);
    public static Menu M3 = new Menu(M3_ID, dateTime, "max burger 100; chiken mix 200; cocatail 50", BK);
    public static Menu M4 = new Menu(M4_ID, dateTime.plusMonths(1), "pizza 100; cesar 200; coffe 50", IL);

    public static final int VOTE1_ID = START_SEQ + 12;
    public static final int VOTE2_ID = START_SEQ + 13;
    public static final int VOTE3_ID = START_SEQ + 14;
    public static final int VOTE4_ID = START_SEQ + 15;

    public static final Vote VOTE1 = new Vote(VOTE1_ID, dateTime, M1, ADMIN);
    public static final Vote VOTE2 = new Vote(VOTE2_ID, dateTime, M2, USER);
    public static final Vote VOTE3 = new Vote(VOTE3_ID, dateTime, M3, USER2);
    public static final Vote VOTE4 = new Vote(VOTE4_ID, dateTime, M4, USER3);

    public static <T> void assertMatchIgnoringFields(String[] ignoringFields, T actual, T expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, ignoringFields);
    }

    public static <T> void assertMatchIgnoringFields(String[] ignoringFields, Iterable<T> actual, T... expected) {
        assertMatchIgnoringFields(ignoringFields, actual, Arrays.asList(expected));
    }

    public static <T> void assertMatchIgnoringFields(String[] ignoringFields, Iterable<T> actual, Iterable<T> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields(ignoringFields).isEqualTo(expected);
    }

    public static <T> void assertMatch(T actual, T expected) {
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    public static <T> void assertMatch(Iterable<T> actual, T... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static <T> void assertMatch(Iterable<T> actual, Iterable<T> expected) {
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }

    public static String jsonWithPassword(JsonUtil jsonUtil, User user, String passw) {
        return jsonUtil.writeAdditionProps(user, "password", passw);
    }


}
