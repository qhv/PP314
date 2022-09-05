package ru.kata.spring.service;

import ru.kata.spring.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> findById(Long id);

    List<User> findAll();

    User create(User user);

    User update(User user);

    boolean delete(Long id);
}
