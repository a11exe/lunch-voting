package ru.alabra.voting.repository;

import ru.alabra.voting.model.Vote;

import java.util.List;

public interface VoteRepository {
    Vote save(Vote vote);

    // false if not found
    boolean delete(int id);

    // null if not found
    Vote get(int id);

    List<Vote> getAll();
}
