package com.example.demo.reward.dto;

import com.example.demo.reward.domain.RewardIssue;
import com.example.demo.reward.domain.RewardType;
import java.time.Instant;
import java.time.LocalDate;

public record RewardIssueResponse(
    Long id,
    Long eventId,
    Long userId,
    Long participationId,
    Long rewardPolicyId,
    RewardType rewardType,
    String rewardValue,
    LocalDate rewardDate,
    Instant issuedDt) {

  public static RewardIssueResponse of(RewardIssue issue) {
    return new RewardIssueResponse(
        issue.getId(),
        issue.getEvent().getId(),
        issue.getUser().getId(),
        issue.getParticipation().getId(),
        issue.getRewardPolicy().getId(),
        issue.getRewardPolicy().getRewardType(),
        issue.getRewardPolicy().getRewardValue(),
        issue.getRewardDate(),
        issue.getIssuedDt());
  }
}
