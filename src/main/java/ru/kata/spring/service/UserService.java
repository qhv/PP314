package ru.kata.spring.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    Optional<User> findById(Long id);

    User findByLogin(String login);

    List<User> findAll();

    User create(User user);

    User update(User user);

    boolean delete(Long id);
}
