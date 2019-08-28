package ru.alabra.voting;

import ru.alabra.voting.model.Role;
import ru.alabra.voting.model.User;
import ru.alabra.voting.model.Vote;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.alabra.voting.MenuTestData.*;
import static ru.alabra.voting.UserTestData.*;
import static ru.alabra.voting.model.AbstractBaseEntity.START_SEQ;

public class VoteTestData {
    public static final int VOTE1_ID = START_SEQ + 12;
    public static final int VOTE2_ID = START_SEQ + 13;
    public static final int VOTE3_ID = START_SEQ + 14;
    public static final int VOTE4_ID = START_SEQ + 15;

    private static String str = "2015-05-30 10:00:00";
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static LocalDateTime dateTime = LocalDateTime.parse(str, formatter);

    public static final Vote VOTE1 = new Vote(VOTE1_ID, dateTime, M1, ADMIN);
    public static final Vote VOTE2 = new Vote(VOTE2_ID, dateTime, M2, USER);
    public static final Vote VOTE3 = new Vote(VOTE3_ID, dateTime, M3, USER2);
    public static final Vote VOTE4 = new Vote(VOTE4_ID, dateTime, M4, USER3);

    public static void assertMatch(Vote actual, Vote expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "registered", "roles");
    }

    public static void assertMatch(Iterable<Vote> actual, Vote... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Vote> actual, Iterable<Vote> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("").isEqualTo(expected);
    }
}
