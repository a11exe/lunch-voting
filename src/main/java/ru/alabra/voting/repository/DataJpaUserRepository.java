package ru.alabra.voting.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.alabra.voting.model.User;

import java.util.List;

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
    public User get(int id) {
        return crudRepository.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public boolean delete(int id) {
        return crudRepository.delete(id) != 0;
    }

    @Override
    public User getByEmail(String email) {
        return crudRepository.getByEmail(email);
    }

    @Override
    public List<User> getAll() {
        return crudRepository.findAll();
    }

}
