package com.example.demo.reward.repository;

import com.example.demo.reward.domain.RewardPolicy;
import com.example.demo.reward.domain.RewardPolicyType;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RewardPolicyRepository extends JpaRepository<RewardPolicy, Long> {
  List<RewardPolicy> findByEvent_IdAndPolicyTypeOrderByTargetOrderAsc(
      Long eventId, RewardPolicyType policyType);

  List<RewardPolicy> findByEvent_IdAndStartDtLessThanEqualAndEndDtGreaterThanEqual(
      Long eventId, LocalDateTime start, LocalDateTime end);
}
