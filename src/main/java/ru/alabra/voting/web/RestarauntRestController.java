package ru.alabra.voting.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.alabra.voting.model.Restaraunt;
import ru.alabra.voting.repository.RestarauntRepository;

import java.net.URI;
import java.util.List;

/**
 * @author Alexander Abramov (alllexe@mail.ru)
 * @version 1
 * @since 26.08.2019
 */

@RestController
@RequestMapping(value = RestarauntRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestarauntRestController {

    @Autowired
    protected RestarauntRepository repository;

    protected final Logger log = LoggerFactory.getLogger(getClass());

    public static final String REST_URL = "/rest/restaurants";

    @GetMapping
    List<Restaraunt> getAll() {
        log.info("getAll");
        return repository.getAll();
    }

    @GetMapping("/{id}")
    public Restaraunt get(@PathVariable int id) {
        log.info("get {}", id);
        return repository.get(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaraunt> createWithLocation(@Validated @RequestBody Restaraunt restaraunt) {
        Restaraunt created = repository.save(restaraunt);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        repository.delete(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@Validated @RequestBody Restaraunt restaraunt) {
        repository.save(restaraunt);
    }

}
