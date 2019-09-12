package ru.alabra.voting.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.alabra.voting.model.Menu;
import ru.alabra.voting.repository.CrudMenuRepository;
import ru.alabra.voting.util.ValidationUtil;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Alexander Abramov (alllexe@mail.ru)
 * @version 1
 * @since 26.08.2019
 */
@RestController
public class MenuRestController {

    public static final String REST_URL = "/rest/menus";

    private final CrudMenuRepository repository;
    private final ValidationUtil validationUtil;
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    public MenuRestController(CrudMenuRepository repository, ValidationUtil validationUtil) {
        this.repository = repository;
        this.validationUtil = validationUtil;
    }

    @GetMapping(value = REST_URL)
    List<Menu> getAll() {
        log.info("findAll menus");
        return repository.findAll();
    }

    @GetMapping(value = REST_URL + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Menu getById(@PathVariable("id") int id) {
        log.info("findById the menu with id={}", id);
        return repository.findById(id)
                .orElseThrow(validationUtil.notFoundWithId("menu id={}", id));
    }

    @GetMapping(value = REST_URL + "/find/by-date", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Menu> getByDate(
            @RequestParam(value = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("findById the menu by date {}", date);
        return repository.findByDate(date);
    }

    @GetMapping(value = REST_URL + "/find/by-period", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Menu> getByPeriod(
            @RequestParam(value = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("findById the menu for the period from {} to {}", startDate, endDate);
        return repository.findByDateBetween(startDate, endDate);
    }

    @GetMapping(value = REST_URL + "/find/by-restaurant", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Menu> getByRestaurant(@RequestParam("id") int restaurantId) {
        log.info("find menu for restaurant with id={}", restaurantId);
        return repository.findByRestaurantId(restaurantId);
    }


    @PostMapping(value = REST_URL, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Menu> create(@Validated @RequestBody Menu menu) {
        // TODO add restaurant consistense check
        validationUtil.checkNew(menu);
        log.info("create {} menu", menu);
        Menu created = repository.save(menu);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping(value = REST_URL + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") int id) {
        log.info("delete menu with id={}", id);
        repository.findById(id)
                .orElseThrow(validationUtil.notFoundWithId("menu id={}", id));
        repository.delete(id);
    }

    @PutMapping(value = REST_URL + "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@PathVariable("id") int id, @Validated @RequestBody Menu menu) {

        // TODO add restaurant consistense check
        Menu updated = repository.findById(id)
                .orElseThrow(validationUtil.notFoundWithId("menu id={}", id));
        validationUtil.assureIdConsistent(updated, id);

        log.info("update menu id={}", menu.getId());
        repository.save(menu);
    }
}
