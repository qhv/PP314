package ru.kata.spring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import ru.kata.spring.model.User;
import ru.kata.spring.service.RoleService;
import ru.kata.spring.service.UserService;

import java.security.Principal;

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
    public String findAll(Model model, Principal principal) {
        model.addAttribute("admin", userService.findByLogin(principal.getName()));
        model.addAttribute("users", userService.findAll());
        model.addAttribute("roles", roleService.findAll());
        return "admin/users";
    }

    @PostMapping("/registration")
    public String create(User user, String rawPassword, Integer[] selectedRoleIds) {
        if (selectedRoleIds != null) userService.create(user, rawPassword, selectedRoleIds);
        return "redirect:/admin";
    }

    @PostMapping("/{id}/update")
    public String update(User user, String rawPassword, Integer[] selectedRoleIds) {
        if (selectedRoleIds != null) {
            userService.update(user, rawPassword, selectedRoleIds);
        }
        return "redirect:/admin";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        if (!userService.delete(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return "redirect:/admin";
    }
}
