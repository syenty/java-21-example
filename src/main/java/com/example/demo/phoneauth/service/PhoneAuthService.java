package com.example.demo.phoneauth.service;

import com.example.demo.phoneauth.dto.PhoneAuthSendRequest;
import com.example.demo.phoneauth.dto.PhoneAuthSendResponse;
import com.example.demo.phoneauth.dto.PhoneAuthVerifyRequest;
import com.example.demo.phoneauth.dto.PhoneAuthVerifyResponse;

public interface PhoneAuthService {

  PhoneAuthSendResponse requestVerification(PhoneAuthSendRequest request);

  PhoneAuthVerifyResponse verifyCode(PhoneAuthVerifyRequest request);
}
