package com.example.demo.phoneauth.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
    name = "phone_auth_request",
    indexes = {
      @Index(name = "idx_phone_created", columnList = "phone_number, created_dt")
    })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PhoneAuthRequest {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "request_id", nullable = false, unique = true, length = 36)
  private String requestId;

  @Column(name = "phone_number", nullable = false, length = 20)
  private String phoneNumber;

  @Column(name = "verification_code", nullable = false, length = 4)
  private String verificationCode;

  @Column(nullable = false, length = 255)
  private String name;

  @Column(name = "employee_number", nullable = false, length = 255)
  private String employeeNumber;

  @Column(name = "created_dt", nullable = false)
  private LocalDateTime createdDt;

  @Column(name = "expired_dt", nullable = false)
  private LocalDateTime expiredDt;

  @Column(nullable = false)
  private boolean verified = false;

  @Column(name = "verified_dt")
  private LocalDateTime verifiedDt;

  @Builder
  private PhoneAuthRequest(
      String requestId,
      String phoneNumber,
      String verificationCode,
      String name,
      String employeeNumber,
      LocalDateTime createdDt,
      LocalDateTime expiredDt,
      boolean verified,
      LocalDateTime verifiedDt) {
    this.requestId = requestId;
    this.phoneNumber = phoneNumber;
    this.verificationCode = verificationCode;
    this.name = name;
    this.employeeNumber = employeeNumber;
    this.createdDt = createdDt;
    this.expiredDt = expiredDt;
    this.verified = verified;
    this.verifiedDt = verifiedDt;
  }

  public boolean isExpired(LocalDateTime now) {
    return expiredDt.isBefore(now);
  }

  public void markVerified(LocalDateTime verifiedDt) {
    this.verified = true;
    this.verifiedDt = verifiedDt;
  }
}
