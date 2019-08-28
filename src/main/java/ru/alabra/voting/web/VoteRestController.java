package ru.alabra.voting.web;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.alabra.voting.model.Menu;
import ru.alabra.voting.model.User;
import ru.alabra.voting.model.Vote;
import ru.alabra.voting.repository.MenuRepository;
import ru.alabra.voting.repository.UserRepository;
import ru.alabra.voting.repository.VoteRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Alexander Abramov (alllexe@mail.ru)
 * @version 1
 * @since 26.08.2019
 */
@RestController
@RequestMapping(value = VoteRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteRestController {

    public static final String REST_URL = "/rest/vote";

    @Autowired
    protected VoteRepository repository;

    @Autowired
    protected MenuRepository repositoryMenu;

    @Autowired
    protected UserRepository repositoryUser;

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping
    public List<Vote> getAll() {
        log.info("getAll");
        return repository.getAll();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void vote(@PathVariable int id) {
        log.info("vote {id}", id);
        LocalDateTime dateTime = LocalDateTime.now().withNano(0);
        if (dateTime.getHour() > 11) {
            //TODO exception
        }
        Menu menu = repositoryMenu.get(id);
        User user = repositoryUser.get(100001);
        Vote vote = repository.getByUserDate(user, dateTime.toLocalDate());
        if (vote == null) {
            vote = new Vote(null, dateTime, menu, user);
        } else {
            vote.setMenu(menu);
        }
        repository.save(vote);
    }

}
