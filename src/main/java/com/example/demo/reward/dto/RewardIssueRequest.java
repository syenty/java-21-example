package com.example.demo.reward.dto;

import java.time.LocalDate;

public record RewardIssueRequest(
    Long eventId, Long userId, Long participationId, Long rewardPolicyId, LocalDate rewardDate) {}
