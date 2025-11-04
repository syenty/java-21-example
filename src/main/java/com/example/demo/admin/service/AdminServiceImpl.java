package com.example.demo.admin.service;

import com.example.demo.admin.domain.Admin;
import com.example.demo.admin.repository.AdminRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {
    private final AdminRepository repository;

    public AdminServiceImpl(AdminRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Admin> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Admin> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Admin create(Admin admin) {
        // Ensure create persists a new entity (id null)
        Admin toSave = new Admin(admin.getName(), admin.getEmail());
        return repository.save(toSave);
    }

    @Override
    public Optional<Admin> update(Long id, Admin admin) {
        return repository.findById(id).map(existing -> {
            existing.setName(admin.getName());
            existing.setEmail(admin.getEmail());
            return repository.save(existing);
        });
    }

    @Override
    public boolean delete(Long id) {
        if (!repository.existsById(id)) return false;
        repository.deleteById(id);
        return true;
    }
}
