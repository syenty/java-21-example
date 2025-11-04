package com.example.demo.user.service;

import com.example.demo.user.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAll();
    Optional<User> getById(Long id);
    User create(User user);
    Optional<User> update(Long id, User user);
    boolean delete(Long id);
}

