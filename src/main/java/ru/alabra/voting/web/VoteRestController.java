package ru.alabra.voting.web;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.alabra.voting.model.*;
import ru.alabra.voting.repository.CrudMenuRepository;
import ru.alabra.voting.repository.CrudUserRepository;
import ru.alabra.voting.repository.CrudVoteRepository;
import ru.alabra.voting.util.SecurityUtil;
import ru.alabra.voting.util.ValidationUtil;
import ru.alabra.voting.util.exception.VotingTimeExpiredException;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

/**
 * @author Alexander Abramov (alllexe@mail.ru)
 * @version 1
 * @since 26.08.2019
 */
@RestController
public class VoteRestController {

    public static final String REST_URL = "/rest/votes";

    private final ValidationUtil validationUtil;
    private final SecurityUtil securityUtil;
    private final CrudVoteRepository repositoryVote;
    private final CrudMenuRepository repositoryMenu;
    private final CrudUserRepository repositoryUser;
    private final ConfigUtil configUtil;
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    public VoteRestController(ValidationUtil validationUtil, SecurityUtil securityUtil, CrudVoteRepository repositoryVote, CrudMenuRepository repositoryMenu, CrudUserRepository repositoryUser, ConfigUtil configUtil) {
        this.validationUtil = validationUtil;
        this.securityUtil = securityUtil;
        this.repositoryVote = repositoryVote;
        this.repositoryMenu = repositoryMenu;
        this.repositoryUser = repositoryUser;
        this.configUtil = configUtil;
    }

    @GetMapping(value = REST_URL)
    public List<Vote> getAll() {
        log.info("find all votes");
        return repositoryVote.findAll();
    }

    @GetMapping(value = REST_URL + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Vote getById(@PathVariable("id") int id) {
        log.info("findById the vote with id={}", id);
        return repositoryVote.findById(id)
                .orElseThrow(validationUtil.notFoundWithId("vote id={}", id));
    }

    @GetMapping(value = REST_URL + "/find/by-date", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Vote> getByDate(@RequestParam(value = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("findById votes by date {}", date);
        return repositoryVote.findByDate(date);
    }

    @GetMapping(value = REST_URL + "/find//by-period", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Vote> getByPeriod(
            @RequestParam(value = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("findById votes by period {}", startDate, endDate);
        return repositoryVote.findByDateBetween(startDate, endDate);
    }

    @GetMapping(value = REST_URL + "/results/by-date", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<VoteResult> getResultsByDate(@RequestParam(value = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("findById votes by date {}", date);
        Map<Restaurant, Integer> voteResults = new HashMap<>();
        List<Vote> votes = repositoryVote.findByDate(date);
        for (Vote vote: votes
             ) {
            Restaurant restaurant = vote.getMenu().getRestaurant();
            Integer voteCount = voteResults.get(restaurant);
            if (voteCount == null) {
                voteResults.put(restaurant, 1);
            } else {
                voteResults.put(restaurant, ++voteCount);
            }
        }
        List<VoteResult> results = new ArrayList<>();
        for (Map.Entry<Restaurant, Integer> entry: voteResults.entrySet()
             ) {
            results.add(new VoteResult(entry.getKey(), entry.getValue()));
        }

        return results;
    }

    @PostMapping(value = REST_URL, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> vote(@RequestBody Integer menuId) {
        log.info("vote for menu id {}", menuId);
        if (LocalTime.now().isAfter(configUtil.getEndVotingTime())) {
            throw new VotingTimeExpiredException("Today the voting time has expired");
        }
        Menu menu = repositoryMenu.findById(menuId).orElseThrow(validationUtil.notFoundWithId("menu id {}", menuId));
        int userId = securityUtil.getAuthUserId();
        User user = repositoryUser.findById(userId).orElseThrow(validationUtil.notFoundWithId("not found user id {}", userId));
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
