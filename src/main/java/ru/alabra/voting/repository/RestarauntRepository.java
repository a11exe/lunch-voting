package ru.alabra.voting.repository;

import ru.alabra.voting.model.Restaurant;

import java.util.List;

public interface RestarauntRepository {

    Restaurant save(Restaurant restaurant);

    // false if not found
    boolean delete(int id);

    // null if not found
    Restaurant get(int id);

    List<Restaurant> getAll();
}
