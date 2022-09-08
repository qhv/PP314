package ru.kata.spring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import ru.kata.spring.model.Role;
import ru.kata.spring.model.User;
import ru.kata.spring.service.RoleService;
import ru.kata.spring.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/users";
    }

    @GetMapping("/registration")
    public String registration(Model model, User user) {
        model.addAttribute("user", user);
        model.addAttribute("nroles", roleService.findAll());
        return "admin/registration";
    }

    @PostMapping("/registration")
    public String create(User user, String rawPassword, Integer[] selectedRoleIds) {
        if (selectedRoleIds == null) return "redirect:/admin/registration";
        userService.create(user, rawPassword, selectedRoleIds);
        return "redirect:/admin";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model) {
        return userService.findById(id)
                .map(user -> {
                    model.addAttribute("user", user);
                    model.addAttribute("roles", roleService.findAll());
                    return "admin/user";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/update")
    public String update(User user, Integer[] selectedRoleIds) {
        if (selectedRoleIds != null) {
            userService.update(user, selectedRoleIds);
        }
        return "redirect:/admin/{id}";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        if (!userService.delete(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return "redirect:/admin";
    }
}
