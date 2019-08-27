package ru.alabra.voting.repository;

import ru.alabra.voting.model.Restaraunt;

import java.util.List;

public interface RestarauntRepository {
    Restaraunt save(Restaraunt restaraunt);

    // false if not found
    boolean delete(int id);

    // null if not found
    Restaraunt get(int id);

    List<Restaraunt> getAll();
}
