package ru.kata.spring.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.kata.spring.model.User;
import ru.kata.spring.service.RoleService;
import ru.kata.spring.service.UserService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminRestController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public List<User> findAll(Principal principal) {
//        model.addAttribute("admin", userService.findByLogin(principal.getName()));
        return userService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(User user, String rawPassword, Integer[] selectedRoleIds) {
//        if (selectedRoleIds != null) throw
        return userService.create(user, rawPassword, selectedRoleIds);
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
