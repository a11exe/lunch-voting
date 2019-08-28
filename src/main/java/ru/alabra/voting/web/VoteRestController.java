package ru.alabra.voting.web;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.alabra.voting.model.Vote;
import ru.alabra.voting.repository.VoteRepository;

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

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping
    List<Vote> getAll() {
        log.info("getAll");
        return repository.getAll();
    }

}
