package com.example.demo.admin.service;

import com.example.demo.admin.domain.Admin;
import com.example.demo.admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminRepository repository;
    private final PasswordEncoder passwordEncoder;

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
        Admin toSave = Admin.builder()
                .name(admin.getName())
                .email(admin.getEmail())
                .password(passwordEncoder.encode(admin.getPassword())) // 비밀번호 암호화
                .role(admin.getRole())
                .build();
        return repository.save(toSave);
    }

    @Override
    public Optional<Admin> update(Long id, Admin admin) {
        return repository.findById(id).map(existing -> {
            existing.update(admin.getName(),
                    admin.getEmail(),
                    passwordEncoder.encode(admin.getPassword()), // 비밀번호 암호화
                    admin.getRole()
            );
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
