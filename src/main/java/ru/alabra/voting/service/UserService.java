package ru.alabra.voting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.alabra.voting.AuthorizedUser;
import ru.alabra.voting.model.User;
import ru.alabra.voting.repository.CrudUserRepository;
import ru.alabra.voting.util.ValidationUtil;
import ru.alabra.voting.util.exception.NotFoundException;

import java.util.List;

@Service("userService")
public class UserService  implements UserDetailsService {

    private final CrudUserRepository repository;

    @Autowired
    private ValidationUtil validationUtil;

    @Autowired
    public UserService(CrudUserRepository repository) {
        this.repository = repository;
    }

    public User create(User user) {
        return repository.save(user);
    }

    public void delete(int id) throws NotFoundException {
        validationUtil.checkNotFoundWithId(repository.delete(id), id);
    }

    public User get(int id) throws NotFoundException {
        return validationUtil.checkNotFoundWithId(repository.findById(id).orElse(null), id);
    }

    public User findByEmail(String email) throws NotFoundException {
        return validationUtil.checkNotFound(repository.findByEmail(email).orElse(null), "email=" + email);
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public void update(User user) throws NotFoundException {
        validationUtil.checkNotFoundWithId(repository.save(user), user.getId());
    }

    @Override
    public AuthorizedUser loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.findByEmail(email.toLowerCase()).orElseThrow(() -> new UsernameNotFoundException("User " + email + " is not found"));
        return new AuthorizedUser(user);
    }
}