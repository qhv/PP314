package ru.kata.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.model.Role;
import ru.kata.spring.service.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String findUser(Model model, Principal principal) {
        var user = userService.findByLogin(principal.getName());
        if (user.getAuthorities().contains(Role.ADMIN)) return "redirect:/admin?user";
        
        model.addAttribute("user", user);

        return "user/user";
    }
}
