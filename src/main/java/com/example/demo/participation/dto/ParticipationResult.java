package com.example.demo.participation.dto;

import com.example.demo.reward.dto.RewardIssueResponse;

public record ParticipationResult(
    EventParticipationResponse participation,
    RewardIssueResponse reward) {}
