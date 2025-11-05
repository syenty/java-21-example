package com.example.demo.admin.controller;

import com.example.demo.admin.domain.Admin;
import com.example.demo.admin.service.AdminService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admins")
public class AdminController {
    private final AdminService service;

    @GetMapping
    public List<Admin> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Admin> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Admin> create(@RequestBody Admin admin) {
        Admin created = service.create(admin);
        return ResponseEntity.created(URI.create("/api/admins/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Admin> update(@PathVariable Long id, @RequestBody Admin admin) {
        return service.update(id, admin)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return service.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
