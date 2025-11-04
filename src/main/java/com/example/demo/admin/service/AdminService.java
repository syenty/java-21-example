package com.example.demo.admin.service;

import com.example.demo.admin.domain.Admin;

import java.util.List;
import java.util.Optional;

public interface AdminService {
    List<Admin> getAll();
    Optional<Admin> getById(Long id);
    Admin create(Admin admin);
    Optional<Admin> update(Long id, Admin admin);
    boolean delete(Long id);
}

