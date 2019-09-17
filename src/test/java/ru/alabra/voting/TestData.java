package ru.alabra.voting;

import ru.alabra.voting.model.*;
import ru.alabra.voting.web.json.JsonUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Alexander Abramov (alllexe@mail.ru)
 * @version 1
 * @since 30.08.2019
 */
public class TestData {

    public static final int ADMIN_ID = 100000;
    public static final int USER_ID = 100001;
    public static final int USER_ID2 = 100002;
    public static final int USER_ID3 = 100003;

    public static final User USER = new User(USER_ID, "User", "user@yandex.ru", "password", Role.ROLE_USER);
    public static final User USER2 = new User(USER_ID2, "Bob", "bob@yandex.ru", "password", Role.ROLE_USER);
    public static final User USER3 = new User(USER_ID3, "Jhon", "jhon@yandex.ru", "password", Role.ROLE_USER);
    public static final User ADMIN = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", Role.ROLE_ADMIN);

    public static final int MC_ID = 100004;
    public static final int KFC_ID = 100005;
    public static final int BK_ID = 100006;
    public static final int IL_ID = 100007;

    public static final Restaurant MC = new Restaurant(MC_ID, "MCDonalds", "mc ddd");
    public static final Restaurant KFC = new Restaurant(KFC_ID, "KFC", "chicken");
    public static final Restaurant BK = new Restaurant(BK_ID, "Burger King", "big burgers");
    public static final Restaurant IL = new Restaurant(IL_ID, "Il Patio", "pizza");

    public static final int M1_ID = 100008;
    public static final int M2_ID = 100009;
    public static final int M3_ID = 100010;
    public static final int M4_ID = 100011;

    private static final String DATE_STRING = "2015-05-30";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final LocalDate DATE_TIME = LocalDate.parse(DATE_STRING, FORMATTER);

    public static final Menu M1 = new Menu(M1_ID, DATE_TIME, "burger 100; coffee 200; potato 50", MC);
    public static final Menu M2 = new Menu(M2_ID, DATE_TIME, "chicken roll 100; black burger 200; potato 50", KFC);
    public static final Menu M3 = new Menu(M3_ID, DATE_TIME, "max burger 100; chicken mix 200; cocktail 50", BK);
    public static final Menu M4 = new Menu(M4_ID, DATE_TIME.plusMonths(1), "pizza 100; cesar 200; coffee 50", IL);

    public static final int VOTE1_ID = 100012;
    public static final int VOTE2_ID = 100013;
    public static final int VOTE3_ID = 100014;
    public static final int VOTE4_ID = 100015;

    public static final Vote VOTE1 = new Vote(VOTE1_ID, DATE_TIME, M1, ADMIN);
    public static final Vote VOTE2 = new Vote(VOTE2_ID, DATE_TIME, M2, USER);
    public static final Vote VOTE3 = new Vote(VOTE3_ID, DATE_TIME, M3, USER2);
    public static final Vote VOTE4 = new Vote(VOTE4_ID, DATE_TIME.plusMonths(1), M4, USER3);

    public static <T> void assertMatchIgnoringFields(String[] ignoringFields, T actual, T expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, ignoringFields);
    }

    public static <T> void assertMatchIgnoringFields(String[] ignoringFields, Iterable<T> actual, T... expected) {
        assertMatchIgnoringFields(ignoringFields, actual, Arrays.asList(expected));
    }

    public static <T> void assertMatchIgnoringFields(String[] ignoringFields, Iterable<T> actual, Iterable<T> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields(ignoringFields).isEqualTo(expected);
    }

    public static <T> void assertMatch(Iterable<T> actual, T... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static <T> void assertMatch(T actual, T expected) {
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    public static <T> void assertMatch(Iterable<T> actual, Iterable<T> expected) {
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }

    public static String jsonWithPassword(JsonUtil jsonUtil, User user, String passw) {
        return jsonUtil.writeAdditionProps(user, "password", passw);
    }


}
