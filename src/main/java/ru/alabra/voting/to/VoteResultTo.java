package ru.alabra.voting.to;

import ru.alabra.voting.model.Restaurant;

/**
 * @author Alexander Abramov (alllexe@mail.ru)
 * @version 1
 * @since 12.09.2019
 */
public class VoteResultTo {

    private final Restaurant restaurant;
    private final Integer votes;

    public VoteResultTo(Restaurant restaurant, Integer votes) {
        this.restaurant = restaurant;
        this.votes = votes;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public Integer getVotes() {
        return votes;
    }
}
