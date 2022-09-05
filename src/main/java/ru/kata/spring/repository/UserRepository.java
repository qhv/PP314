package ru.kata.spring.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.kata.spring.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}
