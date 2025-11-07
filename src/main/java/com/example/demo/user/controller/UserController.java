package com.example.demo.user.controller;

import com.example.demo.user.domain.User;
import com.example.demo.user.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
  private final UserService service;

  @GetMapping
  public List<User> getAll() {
    return service.getAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> getById(@PathVariable Long id) {
    return service.getById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/external/{externalId}")
  public ResponseEntity<User> getByExternalId(@PathVariable String externalId) {
    return service.getByExternalId(externalId)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<User> create(@RequestBody User user) {
    User created = service.create(user);
    return ResponseEntity.created(URI.create("/api/users/" + created.getId())).body(created);
  }

  @PutMapping("/{id}")
  public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User user) {
    return service.update(id, user)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    return service.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
  }
}
