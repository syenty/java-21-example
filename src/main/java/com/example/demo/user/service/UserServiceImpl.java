package com.example.demo.user.service;

import com.example.demo.user.domain.User;
import com.example.demo.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<User> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<User> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public User create(User user) {
        User toSave = User.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
        return repository.save(toSave);
    }

    @Override
    public Optional<User> update(Long id, User user) {
        return repository.findById(id).map(existing -> {
            existing.update(user.getUsername(), user.getEmail());
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
