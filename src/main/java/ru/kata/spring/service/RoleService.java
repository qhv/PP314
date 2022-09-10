package ru.kata.spring.service;

import ru.kata.spring.model.Role;

import java.util.List;

public interface RoleService {

    List<Role> findAll();

    List<Role> findAllById(List<Integer> ids);
}
