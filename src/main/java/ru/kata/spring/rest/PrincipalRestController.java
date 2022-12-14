package ru.kata.spring.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.model.User;

@RestController
@RequestMapping("/api/v1/principal")
public class PrincipalRestController {

    @GetMapping
    public ResponseEntity<User> getPrincipal() {
        return new ResponseEntity<>(
                (User) SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal(),
                HttpStatus.OK
        );
    }
}
