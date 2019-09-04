package ru.alabra.voting.repository;

import ru.alabra.voting.model.User;
import ru.alabra.voting.model.Vote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VoteRepository {
    Vote save(Vote vote);

    // false if not found
    boolean delete(int id);

    Vote getByUserDate(User user, LocalDate date);

    List<Vote> findAll();

    List<Vote> findByDate(LocalDate date);

    List<Vote> findByDateBetween(LocalDate startDate, LocalDate endDate);

    Optional<Vote> findByid(int id);
}
