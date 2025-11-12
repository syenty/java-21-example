package com.example.demo.reward.dto;

import java.time.Instant;
import java.time.LocalDate;

public record RewardIssueExcelRow(
    Instant rewardIssuedDt,
    LocalDate rewardDate,
    String userName,
    String employeeNumber,
    String policyName) {}
