package ru.alabra.voting;

import ru.alabra.voting.model.Restaurant;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.alabra.voting.model.AbstractBaseEntity.START_SEQ;

public class RestarauntTestData {
    public static final int MC_ID = START_SEQ + 4;
    public static final int KFC_ID = START_SEQ + 5;
    public static final int BK_ID = START_SEQ + 6;
    public static final int IL_ID = START_SEQ + 7;

    public static final Restaurant MC = new Restaurant(MC_ID, "MCDonalds", "mc ddd");
    public static final Restaurant KFC = new Restaurant(KFC_ID, "KFC", "chicken");
    public static final Restaurant BK = new Restaurant(BK_ID, "Burger King", "big burgers");
    public static final Restaurant IL = new Restaurant(IL_ID, "Il Patio", "pizza");

    public static void assertMatch(Restaurant actual, Restaurant expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "");
    }

    public static void assertMatch(Iterable<Restaurant> actual, Restaurant... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Restaurant> actual, Iterable<Restaurant> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("").isEqualTo(expected);
    }
}
