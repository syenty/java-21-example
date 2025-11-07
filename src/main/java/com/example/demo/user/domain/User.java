package com.example.demo.user.domain;

import com.example.demo.common.domain.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
public class User extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @Column(name = "phone_number", nullable = false, unique = true)
  private String phoneNumber;

  @Column(nullable = false)
  private String employeeNumber;

  @Column(name = "external_id", nullable = false, unique = true)
  private String externalId;

  public void update(String name, String phoneNumber, String employeeNumber) {
    this.name = name;
    this.phoneNumber = phoneNumber;
    this.employeeNumber = employeeNumber;
  }

  @Builder
  private User(
      final String name,
      final String phoneNumber,
      final String employeeNumber,
      final String externalId) {
    this.name = name;
    this.phoneNumber = phoneNumber;
    this.employeeNumber = employeeNumber;
    this.externalId = externalId;
  }
}
