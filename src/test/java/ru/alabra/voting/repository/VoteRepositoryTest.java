package ru.alabra.voting.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import ru.alabra.voting.model.Vote;

import java.time.LocalDate;
import java.util.List;

import static ru.alabra.voting.TestData.*;

/**
 * @author Alexander Abramov (alllexe@mail.ru)
 * @version 1
 * @since 27.08.2019
 */
class VoteRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    protected CrudVoteRepository repository;

    @Test
    void create() throws Exception {
        LocalDate today = LocalDate.now();
        Vote newVote = new Vote(null, today, M2, USER3);
        Vote created = repository.save(new Vote(newVote));
        newVote.setId(created.getId());
        assertMatch(created, newVote);
        assertMatch(repository.findAll(), VOTE1, VOTE2, VOTE3, VOTE4, newVote);
    }

    @Test
    void delete() throws Exception {
        repository.delete(VOTE1_ID);
        assertMatch(repository.findAll(), VOTE2, VOTE3, VOTE4);
    }

    @Test
    void findById() throws Exception {
        Vote vote = repository.findById(VOTE1_ID).orElse(null);
        assertMatch(vote, VOTE1);
    }

    @Test
    void findByDate() throws Exception {
        List<Vote> votes = repository.findByDate(VOTE1.getDate());
        assertMatch(votes, VOTE1, VOTE2, VOTE3);
    }

    @Test
    void findByPeriod() throws Exception {
        List<Vote> votes = repository.findByDateBetween(VOTE1.getDate(), VOTE4.getDate());
        assertMatch(votes, VOTE1, VOTE2, VOTE3, VOTE4);
    }

    @Test
    void findAll() throws Exception {
        assertMatch(repository.findAll(), VOTE1, VOTE2, VOTE3, VOTE4);
    }

    @Test
    void update() throws Exception {
        Vote updated = new Vote(VOTE1);
        updated.setMenu(M3);
        repository.save(new Vote(updated));
        assertMatch(repository.findById(VOTE1_ID).orElse(null), updated);
    }
}