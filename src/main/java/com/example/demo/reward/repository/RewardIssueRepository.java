package com.example.demo.reward.repository;

import com.example.demo.reward.domain.RewardIssue;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RewardIssueRepository extends JpaRepository<RewardIssue, Long> {
  long countByEvent_IdAndUser_Id(Long eventId, Long userId);

  boolean existsByRewardPolicy_IdAndRewardDate(Long policyId, LocalDate rewardDate);
}
