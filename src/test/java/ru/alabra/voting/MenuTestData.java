package ru.alabra.voting;

import ru.alabra.voting.model.Menu;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.alabra.voting.RestarauntTestData.*;
import static ru.alabra.voting.model.AbstractBaseEntity.START_SEQ;

public class MenuTestData {
    public static final int M1_ID = START_SEQ + 8;
    public static final int M2_ID = START_SEQ + 9;
    public static final int M3_ID = START_SEQ + 10;
    public static final int M4_ID = START_SEQ + 11;

    private static String str = "2015-05-30 10:00:00";
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static LocalDateTime dateTime = LocalDateTime.parse(str, formatter);

    public static Menu M1 = new Menu(M1_ID, dateTime, "burger 100; coffe 200; potato 50", MC);;
    public static Menu M2 = new Menu(M2_ID, dateTime, "chiken roll 100; black burger 200; potato 50", KFC);
    public static Menu M3 = new Menu(M3_ID, dateTime, "max burger 100; chiken mix 200; cocatail 50", BK);
    public static Menu M4 = new Menu(M4_ID, dateTime, "pizza 100; cesar 200; coffe 50", IL);

    public static void assertMatch(Menu actual, Menu expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "");
    }

    public static void assertMatch(Iterable<Menu> actual, Menu... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Menu> actual, Iterable<Menu> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("").isEqualTo(expected);
    }
}
