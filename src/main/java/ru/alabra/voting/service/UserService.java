package ru.alabra.voting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.alabra.voting.AuthorizedUser;
import ru.alabra.voting.model.User;
import ru.alabra.voting.repository.CrudUserRepository;
import ru.alabra.voting.util.ValidationUtil;
import ru.alabra.voting.util.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service("userService")
public class UserService  implements UserDetailsService {

    private final CrudUserRepository repository;

    private final PasswordEncoder passwordEncoder;

    private final ValidationUtil validationUtil;

    @Autowired
    public UserService(CrudUserRepository repository, PasswordEncoder passwordEncoder, ValidationUtil validationUtil) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.validationUtil = validationUtil;
    }

    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }

    public void deleteById(int id) throws NotFoundException {
        repository.deleteById(id);
    }

    public Optional<User> findById(int id) throws NotFoundException {
        return validationUtil.checkNotFoundWithId(repository.findById(id), id);
    }

    public Optional<User> findByEmail(String email) throws NotFoundException {
        return validationUtil.checkNotFound(repository.findByEmail(email), "email=" + email);
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