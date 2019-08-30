package ru.alabra.voting.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import ru.alabra.voting.model.Vote;

import java.time.LocalDateTime;
import java.util.List;

import static ru.alabra.voting.TestData.*;

/**
 * @author Alexander Abramov (alllexe@mail.ru)
 * @version 1
 * @since 27.08.2019
 */
@SpringJUnitWebConfig(locations = {
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-mvc.xml",
        "classpath:spring/spring-db.xml"
})
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
class VoteRepositoryTest {

    @Autowired
    protected VoteRepository repository;

    @Test
    void create() throws Exception {
        LocalDateTime today = LocalDateTime.now().withNano(0);
        Vote newVote = new Vote(null, today, M2, USER3);
        Vote created = repository.save(new Vote(newVote));
        newVote.setId(created.getId());
        assertMatch(created, newVote);
        assertMatch(repository.getAll(), VOTE1, VOTE2, VOTE3, VOTE4, newVote);
    }

    @Test
    void delete() throws Exception {
        repository.delete(VOTE1_ID);
        assertMatch(repository.getAll(), VOTE2, VOTE3, VOTE4);
    }

    @Test
    void get() throws Exception {
        Vote vote = repository.get(VOTE1_ID);
        assertMatch(vote, VOTE1);
    }

    @Test
    void getAll() throws Exception {
        List<Vote> all = repository.getAll();
        assertMatch(repository.getAll(), VOTE1, VOTE2, VOTE3, VOTE4);
    }


    @Test
    void update() throws Exception {
        Vote updated = new Vote(VOTE1);
        updated.setMenu(M3);
        repository.save(new Vote(updated));
        assertMatch(repository.get(VOTE1_ID), updated);
    }
}