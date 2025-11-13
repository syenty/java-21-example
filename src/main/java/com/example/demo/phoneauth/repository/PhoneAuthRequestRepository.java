package com.example.demo.phoneauth.repository;

import com.example.demo.phoneauth.domain.PhoneAuthRequest;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneAuthRequestRepository extends JpaRepository<PhoneAuthRequest, Long> {

  Optional<PhoneAuthRequest> findTopByPhoneNumberOrderByCreatedDtDesc(String phoneNumber);

  Optional<PhoneAuthRequest> findByRequestId(String requestId);
}
