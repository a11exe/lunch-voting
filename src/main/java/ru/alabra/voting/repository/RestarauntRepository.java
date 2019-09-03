package ru.alabra.voting.repository;

import ru.alabra.voting.model.Restaurant;

import java.util.List;
import java.util.Optional;

public interface RestarauntRepository {

    Restaurant save(Restaurant restaurant);

    // false if not found
    boolean delete(int id);

    // null if not found
    Optional<Restaurant> findById(int id);

    List<Restaurant> getAll();
}
