package ru.kata.spring.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    Optional<User> findById(Long id);

    List<User> findAll();

    User create(User user, String rawPassword);

    User update(User user);

    boolean delete(Long id);
}
