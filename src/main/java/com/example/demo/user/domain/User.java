package com.example.demo.user.domain;

import com.example.demo.common.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user", uniqueConstraints = {
    @UniqueConstraint(name = "uk_user_name_employee_number", columnNames = { "name", "employee_number" })
})
public class User extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @Column(name = "phone_number", nullable = false)
  private String phoneNumber;

  @Column(nullable = false)
  private String employeeNumber;

  @Column(name = "branch_code", length = 50)
  private String branchCode;

  @Column(name = "external_id", nullable = false, unique = true)
  private String externalId;

  @Column(nullable = false)
  private boolean blocked = false;

  @Column(name = "phone_verified", nullable = false)
  private boolean phoneVerified = false;

  @Column(name = "phone_verified_dt")
  private Instant phoneVerifiedDt;

  public void update(String name, String employeeNumber, String branchCode) {
    this.name = name;
    this.employeeNumber = employeeNumber;
    this.branchCode = branchCode;
  }

  @Builder
  private User(
      final String name,
      final String phoneNumber,
      final String employeeNumber,
      final String branchCode,
      final String externalId,
      final Boolean blocked,
      final Boolean phoneVerified,
      final Instant phoneVerifiedDt) {
    this.name = name;
    this.phoneNumber = phoneNumber;
    this.employeeNumber = employeeNumber;
    this.branchCode = branchCode;
    this.externalId = externalId;
    this.blocked = blocked != null ? blocked : false;
    this.phoneVerified = phoneVerified != null ? phoneVerified : false;
    this.phoneVerifiedDt = phoneVerifiedDt;
  }

  public void changeBlocked(boolean blocked) {
    this.blocked = blocked;
  }

  public void changePhoneVerification(boolean verified, Instant verifiedDt) {
    this.phoneVerified = verified;
    this.phoneVerifiedDt = verified ? verifiedDt : null;
  }
}
