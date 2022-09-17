package ru.kata.spring.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.kata.spring.model.User;
import ru.kata.spring.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/users")
public class AdminRestController {

    private final UserService userService;

    public AdminRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> findAll() {
        return userService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping("/{id}")
    public User update(User user, String rawPassword, Integer[] selectedRoleIds) {
//        if (selectedRoleIds != null)
        return userService.update(user, rawPassword, selectedRoleIds);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!userService.delete(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
