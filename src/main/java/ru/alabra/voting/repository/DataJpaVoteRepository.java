package ru.alabra.voting.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.alabra.voting.model.Menu;
import ru.alabra.voting.model.User;
import ru.alabra.voting.model.Vote;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public class DataJpaVoteRepository implements VoteRepository {

    @Autowired
    private CrudVoteRepository crudRepository;

    @Override
    @Transactional
    public Vote save(Vote vote) {
        return crudRepository.save(vote);
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return crudRepository.delete(id) != 0;
    }

    @Override
    public Vote get(int id) {
        return crudRepository.findById(id).orElse(null);
    }

    @Override
    public List<Vote> getAll() {
        return crudRepository.findAll();
    }

    @Override
    public Vote getByUserDate(User user, LocalDate date) {
        return crudRepository.getByUserDate(user.getId(), date.atStartOfDay(), date.atTime(LocalTime.MAX)).stream().findFirst().orElse(null);
    }
}