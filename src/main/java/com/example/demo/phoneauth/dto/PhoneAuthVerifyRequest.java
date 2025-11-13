package com.example.demo.phoneauth.dto;

public record PhoneAuthVerifyRequest(
    String phoneNumber,
    String verificationCode) {}
