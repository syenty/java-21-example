package com.example.demo.phoneauth.controller;

import com.example.demo.phoneauth.dto.PhoneAuthSendRequest;
import com.example.demo.phoneauth.dto.PhoneAuthSendResponse;
import com.example.demo.phoneauth.dto.PhoneAuthVerifyRequest;
import com.example.demo.phoneauth.dto.PhoneAuthVerifyResponse;
import com.example.demo.phoneauth.service.PhoneAuthService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/phone-auth")
@RequiredArgsConstructor
public class PhoneAuthController {

  private final PhoneAuthService phoneAuthService;

  @PostMapping("/request")
  public ResponseEntity<PhoneAuthSendResponse> request(@RequestBody PhoneAuthSendRequest request) {
    return ResponseEntity.ok(phoneAuthService.requestVerification(request));
  }

  @PostMapping("/verify")
  public ResponseEntity<PhoneAuthVerifyResponse> verify(@RequestBody PhoneAuthVerifyRequest request) {
    return ResponseEntity.ok(phoneAuthService.verifyCode(request));
  }
}
