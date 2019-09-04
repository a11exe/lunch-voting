package ru.alabra.voting.repository;

import ru.alabra.voting.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);

    // false if not found
    boolean delete(int id);

    // null if not found
    Optional<User> findById(int id);

    // null if not found
    Optional<User> findByEmail(String email);

    List<User> findAll();
}
