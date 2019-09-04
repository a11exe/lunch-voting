package ru.alabra.voting.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.alabra.voting.model.User;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class DataJpaUserRepository implements UserRepository {

    @Autowired
    private CrudUserRepository crudRepository;

    @Override
    @Transactional
    public User save(User user) {
        return crudRepository.save(user);
    }

    @Override
    public Optional<User> findById(int id) {
        return crudRepository.findById(id);
    }

    @Transactional
    @Override
    public boolean delete(int id) {
        return crudRepository.delete(id) != 0;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return crudRepository.findByEmail(email);
    }

    @Override
    public List<User> findAll() {
        return crudRepository.findAll();
    }

}
