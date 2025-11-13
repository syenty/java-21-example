package com.example.demo.phoneauth.dto;

public record PhoneAuthSendRequest(
    String name,
    String employeeNumber,
    String phoneNumber) {}
