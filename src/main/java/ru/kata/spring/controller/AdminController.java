package ru.kata.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
        model.addAttribute("roles", roleService.findAll());
        return "admin/users";
    }
}
