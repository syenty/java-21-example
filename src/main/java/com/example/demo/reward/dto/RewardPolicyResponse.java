package com.example.demo.reward.dto;

import com.example.demo.reward.domain.RewardPolicy;
import com.example.demo.reward.domain.RewardPolicyNthScope;
import com.example.demo.reward.domain.RewardPolicyType;
import com.example.demo.reward.domain.RewardType;
import java.time.Instant;
import java.time.LocalDateTime;

public record RewardPolicyResponse(
    Long id,
    Long eventId,
    String name,
    RewardPolicyType policyType,
    LocalDateTime startDt,
    LocalDateTime endDt,
    Integer winnerLimitTotal,
    Integer winnerLimitPerDay,
    Integer targetOrder,
    RewardPolicyNthScope nthScope,
    Integer userLimitTotal,
    Integer userLimitPerDay,
    RewardType rewardType,
    String rewardValue,
    Instant createdDt,
    Instant updatedDt) {

  public static RewardPolicyResponse of(RewardPolicy policy) {
    return new RewardPolicyResponse(
        policy.getId(),
        policy.getEvent().getId(),
        policy.getName(),
        policy.getPolicyType(),
        policy.getStartDt(),
        policy.getEndDt(),
        policy.getWinnerLimitTotal(),
        policy.getWinnerLimitPerDay(),
        policy.getTargetOrder(),
        policy.getNthScope(),
        policy.getUserLimitTotal(),
        policy.getUserLimitPerDay(),
        policy.getRewardType(),
        policy.getRewardValue(),
        policy.getCreatedDt(),
        policy.getUpdatedDt());
  }
}
