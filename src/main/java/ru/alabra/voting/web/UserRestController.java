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
import ru.alabra.voting.model.User;
import ru.alabra.voting.service.UserService;
import ru.alabra.voting.util.ValidationUtil;

import java.net.URI;
import java.util.List;

/**
 * @author Alexander Abramov (alllexe@mail.ru)
 * @version 1
 * @since 26.08.2019
 */

@RestController
@RequestMapping(value = UserRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserRestController {

    public static final String REST_URL = "/rest/admin/users";

    private final UserService service;
    private final ValidationUtil validationUtil;
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    public UserRestController(UserService service, ValidationUtil validationUtil) {
        this.service = service;
        this.validationUtil = validationUtil;
    }

    @GetMapping
    List<User> getAll() {
        log.info("find all users");
        return service.findAll();
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable int id) {
        log.info("find user by id {}", id);
        return service.findById(id).orElse(null);
    }

    @GetMapping("/find/by-email")
    public User getByEmail(@RequestParam String email) {
        log.info("find user by email {}", email);
        return service.findByEmail(email).orElse(null);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> create(@Validated @RequestBody User user) {
        log.info("create user");
        User created = service.save(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete user by id {}", id);
        service.delete(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@Validated @RequestBody User user, @PathVariable("id") int id) {
        validationUtil.assureIdConsistent(user, id);
        log.info("update user id {}", id);
        service.save(user);
    }

}
