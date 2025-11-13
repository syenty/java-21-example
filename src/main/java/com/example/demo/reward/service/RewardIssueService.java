package com.example.demo.reward.service;

import com.example.demo.participation.domain.EventParticipation;
import com.example.demo.reward.domain.RewardPolicy;
import com.example.demo.reward.dto.RewardIssueRequest;
import com.example.demo.reward.dto.RewardIssueResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RewardIssueService {
  List<RewardIssueResponse> findAll();

  Optional<RewardIssueResponse> findById(Long id);

  Optional<RewardIssueResponse> create(RewardIssueRequest request);

  Optional<RewardIssueResponse> update(Long id, RewardIssueRequest request);

  boolean delete(Long id);

  Optional<RewardIssueResponse> decideAndIssue(
      List<RewardPolicy> policies, EventParticipation participation, LocalDate rewardDate);

  List<RewardIssueResponse> findByEventAndPeriod(Long eventId, Instant start, Instant end);

  void downloadExcel(Long eventId, Instant start, Instant end, HttpServletResponse response);
}
