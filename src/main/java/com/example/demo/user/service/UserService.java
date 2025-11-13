package com.example.demo.user.service;

import com.example.demo.common.dto.PageWrapper;
import com.example.demo.user.domain.User;
import com.example.demo.user.dto.UserUpdateRequest;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface UserService {
    PageWrapper<User> search(String name, String employeeNumber, Pageable pageable);
    Optional<User> getById(Long id);
    Optional<User> getByExternalId(String externalId);
    Optional<User> getByEmployeeNumberAndName(String employeeNumber, String name);
    User getRequiredByEmployeeNumberAndName(String employeeNumber, String name);
    User create(User user);
    Optional<User> update(Long id, UserUpdateRequest request);
    Optional<User> updateBlocked(Long id, boolean blocked);
    boolean delete(Long id);
}
