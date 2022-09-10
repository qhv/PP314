package ru.kata.spring.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.kata.spring.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u join fetch u.roles r where u.login = :username")
    Optional<User> findByLogin(String username);

    // Без entity graph не происходит подгрузка ролей пользователя
    // на страничке редактирования пользователся
    @EntityGraph(attributePaths = "roles")
    Optional<User> findUserById(Long id);

    // Без entity graph не происходит подгрузка ролей пользователя
    // на страничке вывода всех пользователей
    @EntityGraph(attributePaths = "roles")
    List<User> findAll();

    @Query("select u from User u join fetch u.roles r where u.id = :id")
    User findByIdIfUserExists(Long id);
}
