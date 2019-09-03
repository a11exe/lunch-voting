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
import ru.alabra.voting.repository.MenuRepository;
import ru.alabra.voting.repository.RestarauntRepository;
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

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private RestarauntRepository restarauntRepository;

    @Autowired
    private ValidationUtil validationUtil;

    protected final Logger log = LoggerFactory.getLogger(getClass());

    public static final String REST_URL_RESTAURANT_MENU = "/rest/restaurants/{restaurantId}/menu";
    public static final String REST_URL_RESTAURANTS = "/rest/restaurants/menu";

    @PostMapping(value = REST_URL_RESTAURANT_MENU, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Menu> create(@PathVariable("restaurantId") int restaurantId, @Validated @RequestBody Menu menu) {

        menu.setRestaurant(restarauntRepository.findById(restaurantId)
                .orElseThrow(validationUtil.notFoundWithId("restaurant id {}", restaurantId)));
        validationUtil.checkNew(menu);

        log.info("create {} for restaurant with id={}", menu, restaurantId);
        Menu created = menuRepository.save(menu);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL_RESTAURANT_MENU + "/{id}")
                .buildAndExpand(created.getRestaurant().getId(), created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping(value = REST_URL_RESTAURANT_MENU + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("restaurantId") int restaurantId, @PathVariable("id") int id) {
        log.info("delete menu with id={} for restaurant with id={}", id, restaurantId);
        menuRepository.findByRestaurantIdAndId(restaurantId, id)
                .orElseThrow(validationUtil.notFoundWithId("restaurant id {} menu id={}", restaurantId, id));

        menuRepository.delete(id);
    }

    @PutMapping(value = REST_URL_RESTAURANT_MENU + "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@PathVariable("restaurantId") int restaurantId, @PathVariable("id") int id, @Validated @RequestBody Menu menu) {

        Menu updated = menuRepository.findByRestaurantIdAndId(restaurantId, id)
                .orElseThrow(validationUtil.notFoundWithId("restaurant id {} menu id={}", restaurantId, id));

        validationUtil.assureIdConsistent(updated, id);

        log.info("update {} for restaurant with id={}", menu, restaurantId);
        menu.setRestaurant(restarauntRepository.findById(restaurantId)
                .orElseThrow(validationUtil.notFoundWithId("restaurant id {}", restaurantId)));

        menuRepository.save(menu);
    }

    @GetMapping(value = REST_URL_RESTAURANTS + "/by-date", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Menu> getByAllRestaurantsAndByDate(
            @RequestParam(value = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("get the menu by date {}", date);
        return menuRepository.findByDate(date);
    }

    @GetMapping(value = REST_URL_RESTAURANTS + "/by-period", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Menu> getByAllRestaurantsAndByPeriod(
            @RequestParam(value = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("get the menu for the period from {} to {}", startDate, endDate);
        return menuRepository.findByDateBetween(startDate, endDate);
    }

    @GetMapping(value = REST_URL_RESTAURANT_MENU, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Menu> getAllByRestaurant(@PathVariable("restaurantId") int restaurantId) {
        log.info("get the menu for restaurant with id={}", restaurantId);
        return menuRepository.findByRestaurantId(restaurantId);
    }

    @GetMapping(value = REST_URL_RESTAURANT_MENU + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Menu get(@PathVariable("restaurantId") int restaurantId, @PathVariable("id") int id) {
        log.info("get the menu with id={} for restaurant with id={}", id, restaurantId);
        return menuRepository.findByRestaurantIdAndId(restaurantId, id)
                .orElseThrow(validationUtil.notFoundWithId("restaurant id {} menu id={}", id, restaurantId));
    }

    @GetMapping(value = REST_URL_RESTAURANT_MENU + "/by-date", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Menu> getByRestaurantAndByDate(
            @PathVariable("restaurantId") int restaurantId,
            @RequestParam(value = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("get the menu by date {} for restaurant with id={}", date, restaurantId);
        return menuRepository.findByRestaurantIdAndDate(restaurantId, date);
    }

    @GetMapping(value = REST_URL_RESTAURANT_MENU + "/by-period", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Menu> getByRestaurantAndByPeriod(
            @PathVariable("restaurantId") int restaurantId,
            @RequestParam(value = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("get the menu for the period from {} to {} for restaurant with id={}",
                startDate, endDate, restaurantId);
        return menuRepository.findByRestaurantIdAndDateBetween(restaurantId, startDate, endDate);
    }

}
