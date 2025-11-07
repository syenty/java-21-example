package com.example.demo.auth.controller;

import com.example.demo.auth.service.AuthService;
import com.example.demo.common.auth.dto.LoginRequest;
import com.example.demo.common.auth.dto.TokenResponse;
import com.example.demo.common.auth.dto.UserEmployeeLoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;

  @PostMapping("/admin-login")
  public ResponseEntity<TokenResponse> adminLogin(@RequestBody LoginRequest loginRequest) {
    return ResponseEntity.ok(authService.adminLogin(loginRequest));
  }

  @PostMapping("/user-login")
  public ResponseEntity<TokenResponse> userLogin(@RequestBody UserEmployeeLoginRequest request) {
    return ResponseEntity.ok(authService.userLogin(request));
  }
}
