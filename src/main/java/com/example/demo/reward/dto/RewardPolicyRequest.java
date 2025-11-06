package com.example.demo.reward.dto;

import com.example.demo.reward.domain.RewardPolicyNthScope;
import com.example.demo.reward.domain.RewardPolicyType;
import com.example.demo.reward.domain.RewardType;
import java.time.LocalDateTime;

public record RewardPolicyRequest(
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
    String rewardValue) {}
