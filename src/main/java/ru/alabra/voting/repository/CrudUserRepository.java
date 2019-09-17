package ru.alabra.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.alabra.voting.model.User;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface CrudUserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

}
