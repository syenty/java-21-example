package com.example.demo.user.service;

import com.example.demo.user.domain.User;
import com.example.demo.user.dto.UserUpdateRequest;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAll();
    Optional<User> getById(Long id);
    Optional<User> getByExternalId(String externalId);
    Optional<User> getByEmployeeNumberAndName(String employeeNumber, String name);
    User getRequiredByEmployeeNumberAndName(String employeeNumber, String name);
    User create(User user);
    Optional<User> update(Long id, UserUpdateRequest request);
    Optional<User> updateBlocked(Long id, boolean blocked);
    boolean delete(Long id);
}
