package ru.alabra.voting.web;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.alabra.voting.model.Menu;
import ru.alabra.voting.model.User;
import ru.alabra.voting.model.Vote;
import ru.alabra.voting.repository.CrudMenuRepository;
import ru.alabra.voting.repository.CrudUserRepository;
import ru.alabra.voting.repository.CrudVoteRepository;
import ru.alabra.voting.util.ConfigUtil;
import ru.alabra.voting.util.SecurityUtil;
import ru.alabra.voting.util.ValidationUtil;
import ru.alabra.voting.util.exception.VotingTimeExpiredException;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

/**
 * @author Alexander Abramov (alllexe@mail.ru)
 * @version 1
 * @since 26.08.2019
 */
@RestController
public class VoteRestController {

    public static final String REST_URL = "/rest/vote";

    @Autowired
    private ValidationUtil validationUtil;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    protected CrudVoteRepository repositoryVote;

    @Autowired
    protected CrudMenuRepository repositoryMenu;

    @Autowired
    protected CrudUserRepository repositoryUser;

    @Autowired
    protected ConfigUtil configUtil;

    private final Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping(value = REST_URL)
    public List<Vote> findAll() {
        log.info("find all votes");
        return repositoryVote.findAll();
    }

    @GetMapping(value = REST_URL + "/by-date", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Vote> getByDate(@RequestParam(value = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("findById votes by date {}", date);
        return repositoryVote.findByDate(date);
    }

    @GetMapping(value = REST_URL + "/by-period", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Vote> getByPeriod(
            @RequestParam(value = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("findById votes by period {}", startDate, endDate);
        return repositoryVote.findByDateBetween(startDate, endDate);
    }

    @PostMapping(value = REST_URL, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> vote(@RequestBody Integer menuId) {
        log.info("vote for menu id {}", menuId);
        if (LocalTime.now().isAfter(configUtil.getEndVotingTime())) {
            throw new VotingTimeExpiredException("Today the voting time has expired");
        }
        Menu menu = repositoryMenu.findById(menuId).orElseThrow(validationUtil.notFoundWithId("menu id {}", menuId));
        User user = repositoryUser.findById(securityUtil.getAuthUserId()).orElse(null);
        LocalDate today = LocalDate.now();
        Vote vote = repositoryVote.findByUserAndByDate(Objects.requireNonNull(user).getId(), today).stream().findFirst().orElse(null);
        if (vote == null) {
            vote = new Vote(null, today, menu, user);
        } else {
            vote.setMenu(menu);
        }
        Vote created = repositoryVote.save(vote);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId(), created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

}
