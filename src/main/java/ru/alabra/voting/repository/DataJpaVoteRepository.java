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
import java.util.Optional;

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
    public Vote getByUserDate(User user, LocalDate date) {
        return crudRepository.getByUserDate(user.getId(), date).stream().findFirst().orElse(null);
    }

    @Override
    public List<Vote> findByDate(LocalDate date) {
        return crudRepository.findByDate(date);
    }

    @Override
    public List<Vote> findByDateBetween(LocalDate startDate, LocalDate endDate) {
        return crudRepository.findByDateBetween(startDate, endDate);
    }

    @Override
    public List<Vote> findAll() {
        return crudRepository.findAll();
    }

    @Override
    public Optional<Vote> findByid(int id) {
        return crudRepository.findById(id);
    }
}
