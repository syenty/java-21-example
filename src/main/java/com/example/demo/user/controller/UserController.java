package com.example.demo.user.controller;

import com.example.demo.common.dto.PageWrapper;
import com.example.demo.user.domain.User;
import com.example.demo.user.dto.UserBlockedRequest;
import com.example.demo.user.dto.UserLookupResponse;
import com.example.demo.user.dto.UserUpdateRequest;
import com.example.demo.user.dto.UserSearchParam;
import com.example.demo.user.service.UserService;

import lombok.RequiredArgsConstructor;

import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
  private final UserService service;

  @GetMapping
  public PageWrapper<User> getAll(@Valid @ModelAttribute UserSearchParam params) {
    return service.search(params.getName(), params.getEmployeeNumber(), PageRequest.of(params.getPage(), params.getSize()));
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> getById(@PathVariable Long id) {
    return service.getById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }
 
  @GetMapping("/lookup")
  public ResponseEntity<UserLookupResponse> findOrCreate(
      @RequestParam String name,
      @RequestParam String employeeNumber) {
    User user = service.getRequiredByEmployeeNumberAndName(employeeNumber, name);
    return ResponseEntity.ok(new UserLookupResponse(user.getExternalId()));
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
  public ResponseEntity<User> update(@PathVariable Long id, @RequestBody UserUpdateRequest request) {
    return service.update(id, request)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PatchMapping("/{id}/blocked")
  public ResponseEntity<User> updateBlocked(@PathVariable Long id, @RequestBody UserBlockedRequest request) {
    return service.updateBlocked(id, request.blocked())
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    return service.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
  }
}
