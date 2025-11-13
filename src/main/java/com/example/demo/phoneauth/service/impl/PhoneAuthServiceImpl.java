package com.example.demo.phoneauth.service.impl;

import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.common.util.StringUtil;
import com.example.demo.phoneauth.domain.PhoneAuthRequest;
import com.example.demo.phoneauth.dto.PhoneAuthSendRequest;
import com.example.demo.phoneauth.dto.PhoneAuthSendResponse;
import com.example.demo.phoneauth.dto.PhoneAuthVerifyRequest;
import com.example.demo.phoneauth.dto.PhoneAuthVerifyResponse;
import com.example.demo.phoneauth.repository.PhoneAuthRequestRepository;
import com.example.demo.phoneauth.service.PhoneAuthService;
import com.example.demo.user.domain.User;
import com.example.demo.user.repository.UserRepository;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PhoneAuthServiceImpl implements PhoneAuthService {

  private static final int CODE_LENGTH = 4;
  private static final long EXPIRE_MINUTES = 5L;

  private final PhoneAuthRequestRepository phoneAuthRequestRepository;
  private final UserRepository userRepository;

  @Override
  @Transactional
  public PhoneAuthSendResponse requestVerification(PhoneAuthSendRequest request) {
    LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
    LocalDateTime expired = now.plusMinutes(EXPIRE_MINUTES);
    String verificationCode = StringUtil.randomNumericCode(CODE_LENGTH);

    PhoneAuthRequest entity = PhoneAuthRequest.builder()
        .requestId(UUID.randomUUID().toString())
        .phoneNumber(request.phoneNumber())
        .verificationCode(verificationCode)
        .name(request.name())
        .employeeNumber(request.employeeNumber())
        .createdDt(now)
        .expiredDt(expired)
        .verified(false)
        .build();

    phoneAuthRequestRepository.save(entity);
    log.info("Send verification code={} to phone={}", verificationCode, request.phoneNumber());

    // TODO: 문자 발송

    return new PhoneAuthSendResponse(entity.getRequestId());
  }

  @Override
  @Transactional
  public PhoneAuthVerifyResponse verifyCode(PhoneAuthVerifyRequest request) {
    PhoneAuthRequest latest = phoneAuthRequestRepository
        .findTopByPhoneNumberOrderByCreatedDtDesc(request.phoneNumber())
        .orElseThrow(() -> new BusinessException(ErrorCode.PHONE_AUTH_NOT_FOUND));

    LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
    if (latest.isExpired(now)) {
      throw new BusinessException(ErrorCode.PHONE_AUTH_EXPIRED);
    }
    if (!latest.getVerificationCode().equals(request.verificationCode())) {
      throw new BusinessException(ErrorCode.PHONE_AUTH_INVALID_CODE);
    }

    latest.markVerified(now);
    phoneAuthRequestRepository.save(latest);

    User user = upsertUser(latest, now);
    return new PhoneAuthVerifyResponse(user.getExternalId());
  }

  private User upsertUser(PhoneAuthRequest latest, LocalDateTime verifiedAt) {
    Optional<User> existingOpt = userRepository
        .findByEmployeeNumberAndName(latest.getEmployeeNumber(), latest.getName());

    Instant verifiedInstant = verifiedAt.toInstant(ZoneOffset.UTC);
    if (existingOpt.isPresent()) {
      User existing = existingOpt.get();
      if (!existing.isPhoneVerified()) {
        existing.changePhoneVerification(true, verifiedInstant);
        userRepository.save(existing);
      }
      return existing;
    }

    User newUser = User.builder()
        .name(latest.getName())
        .phoneNumber(latest.getPhoneNumber())
        .employeeNumber(latest.getEmployeeNumber())
        .branchCode(null)
        .externalId(UUID.randomUUID().toString())
        .blocked(false)
        .phoneVerified(true)
        .phoneVerifiedDt(verifiedInstant)
        .build();
    return userRepository.save(newUser);
  }

}
