package ru.alabra.voting.model;

/**
 * @author Alexander Abramov (alllexe@mail.ru)
 * @version 1
 * @since 12.09.2019
 */
public class VoteResult {

    private final Restaurant restaurant;
    private final Integer votes;

    public VoteResult(Restaurant restaurant, Integer votes) {
        this.restaurant = restaurant;
        this.votes = votes;
    }
}
