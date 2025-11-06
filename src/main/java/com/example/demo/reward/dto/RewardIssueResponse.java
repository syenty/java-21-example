package com.example.demo.reward.dto;

import com.example.demo.reward.domain.RewardIssue;
import java.time.Instant;
import java.time.LocalDate;

public record RewardIssueResponse(
    Long id,
    Long eventId,
    Long userId,
    Long participationId,
    Long rewardPolicyId,
    LocalDate rewardDate,
    Instant issuedDt) {

  public static RewardIssueResponse of(RewardIssue issue) {
    return new RewardIssueResponse(
        issue.getId(),
        issue.getEvent().getId(),
        issue.getUser().getId(),
        issue.getParticipation().getId(),
        issue.getRewardPolicy().getId(),
        issue.getRewardDate(),
        issue.getIssuedDt());
  }
}
