package ru.kata.spring.service;

import org.springframework.stereotype.Service;
import ru.kata.spring.model.Role;

import java.util.List;

public interface RoleService {

    List<Role> findAll();
}
