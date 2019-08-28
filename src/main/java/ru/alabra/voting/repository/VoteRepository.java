package ru.alabra.voting.repository;

import ru.alabra.voting.model.User;
import ru.alabra.voting.model.Vote;

import java.time.LocalDate;
import java.util.List;

public interface VoteRepository {
    Vote save(Vote vote);

    // false if not found
    boolean delete(int id);

    // null if not found
    Vote get(int id);

    Vote getByUserDate(User user, LocalDate date);

    List<Vote> getAll();
}
