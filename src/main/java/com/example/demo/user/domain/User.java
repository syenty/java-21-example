package com.example.demo.user.domain;

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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "organization_code", nullable = false)
    private String organizationCode;

    public void update(String name, String phoneNumber, String organizationCode) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.organizationCode = organizationCode;
    }

    @Builder
    private User(final String name, final String phoneNumber, final String organizationCode) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.organizationCode = organizationCode;
    }
}
