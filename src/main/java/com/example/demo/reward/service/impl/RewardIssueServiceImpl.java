package com.example.demo.reward.service.impl;

import com.example.demo.common.util.ExcelUtil;
import com.example.demo.common.util.StringUtil;
import com.example.demo.event.domain.Event;
import com.example.demo.event.repository.EventRepository;
import com.example.demo.participation.domain.EventParticipation;
import com.example.demo.participation.repository.EventParticipationRepository;
import com.example.demo.reward.domain.RewardIssue;
import com.example.demo.reward.domain.RewardPolicy;
import com.example.demo.reward.dto.RewardIssueExcelRow;
import com.example.demo.reward.dto.RewardIssueRequest;
import com.example.demo.reward.dto.RewardIssueResponse;
import com.example.demo.reward.repository.RewardIssueRepository;
import com.example.demo.reward.repository.RewardPolicyRepository;
import com.example.demo.reward.service.RewardIssueService;
import com.example.demo.user.domain.User;
import com.example.demo.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RewardIssueServiceImpl implements RewardIssueService {

  private final RewardIssueRepository rewardIssueRepository;
  private final EventRepository eventRepository;
  private final UserRepository userRepository;
  private final EventParticipationRepository participationRepository;
  private final RewardPolicyRepository rewardPolicyRepository;

  private static final List<String> ISSUE_HEADERS = List.of(
      "rewardIssuedDt",
      "rewardDate",
      "userName",
      "employeeNumber",
      "policyName");

  @Override
  public List<RewardIssueResponse> findAll() {
    return rewardIssueRepository.findAll().stream().map(RewardIssueResponse::of).toList();
  }

  @Override
  public Optional<RewardIssueResponse> findById(Long id) {
    return rewardIssueRepository.findById(id).map(RewardIssueResponse::of);
  }

  @Override
  @Transactional
  public Optional<RewardIssueResponse> create(RewardIssueRequest request) {
    Optional<Event> eventOpt = eventRepository.findById(request.eventId());
    if (eventOpt.isEmpty()) {
      return Optional.empty();
    }
    Optional<User> userOpt = userRepository.findById(request.userId());
    if (userOpt.isEmpty()) {
      return Optional.empty();
    }
    Optional<EventParticipation> participationOpt = participationRepository.findById(request.participationId());
    if (participationOpt.isEmpty()) {
      return Optional.empty();
    }
    Optional<RewardPolicy> policyOpt = rewardPolicyRepository.findById(request.rewardPolicyId());
    if (policyOpt.isEmpty()) {
      return Optional.empty();
    }

    RewardIssue issue = RewardIssue.builder()
        .event(eventOpt.get())
        .user(userOpt.get())
        .participation(participationOpt.get())
        .rewardPolicy(policyOpt.get())
        .rewardDate(request.rewardDate())
        .build();
    return Optional.of(RewardIssueResponse.of(rewardIssueRepository.save(issue)));
  }

  @Override
  @Transactional
  public Optional<RewardIssueResponse> update(Long id, RewardIssueRequest request) {
    return rewardIssueRepository
        .findById(id)
        .flatMap(
            issue -> {
              if (request.eventId() != null
                  && !issue.getEvent().getId().equals(request.eventId())) {
                Optional<Event> eventOpt = eventRepository.findById(request.eventId());
                if (eventOpt.isEmpty()) {
                  return Optional.<RewardIssueResponse>empty();
                }
                issue.changeEvent(eventOpt.get());
              }
              if (request.userId() != null
                  && !issue.getUser().getId().equals(request.userId())) {
                Optional<User> userOpt = userRepository.findById(request.userId());
                if (userOpt.isEmpty()) {
                  return Optional.<RewardIssueResponse>empty();
                }
                issue.changeUser(userOpt.get());
              }
              if (request.participationId() != null
                  && !issue.getParticipation().getId().equals(request.participationId())) {
                Optional<EventParticipation> participationOpt = participationRepository
                    .findById(request.participationId());
                if (participationOpt.isEmpty()) {
                  return Optional.<RewardIssueResponse>empty();
                }
                issue.changeParticipation(participationOpt.get());
              }
              if (request.rewardPolicyId() != null
                  && !issue.getRewardPolicy().getId().equals(request.rewardPolicyId())) {
                Optional<RewardPolicy> policyOpt = rewardPolicyRepository.findById(request.rewardPolicyId());
                if (policyOpt.isEmpty()) {
                  return Optional.<RewardIssueResponse>empty();
                }
                issue.changeRewardPolicy(policyOpt.get());
              }
              issue.update(request.rewardDate(), null);
              return Optional.of(RewardIssueResponse.of(issue));
            });
  }

  @Override
  @Transactional
  public boolean delete(Long id) {
    if (!rewardIssueRepository.existsById(id)) {
      return false;
    }
    rewardIssueRepository.deleteById(id);
    return true;
  }

  @Override
  @Transactional
  public Optional<RewardIssueResponse> decideAndIssue(List<RewardPolicy> policies,
      EventParticipation participation,
      LocalDate rewardDate) {
    if (policies == null || policies.isEmpty()) {
      return Optional.empty();
    }

    Event event = participation.getEvent();
    if (hasReachedEventRewardLimit(event, participation.getUser().getId())) {
      return Optional.empty();
    }

    for (RewardPolicy policy : policies) {
      Optional<RewardIssueResponse> issued = issueNthOrder(policy, participation, rewardDate);
      if (issued.isPresent()) {
        return issued;
      }
    }
    return Optional.empty();
  }

  private boolean hasReachedEventRewardLimit(Event event, Long userId) {
    Integer limit = event.getRewardLimitPerUser();
    if (limit == null || limit <= 0) {
      return false;
    }
    long issuedCount = rewardIssueRepository.countByEvent_IdAndUser_Id(event.getId(), userId);
    return issuedCount >= limit;
  }

  private Optional<RewardIssueResponse> issueNthOrder(RewardPolicy policy,
      EventParticipation participation, LocalDate rewardDate) {
    Integer targetOrder = policy.getTargetOrder();
    if (targetOrder == null) {
      return Optional.empty();
    }

    long order = determineParticipationOrder(policy, participation);
    if (order < targetOrder) {
      return Optional.empty();
    }

    if (hasWinnerForScope(policy, rewardDate)) {
      return Optional.empty();
    }

    if (exceedsUserPolicyLimit(policy, participation.getUser().getId(), rewardDate)) {
      return Optional.empty();
    }

    RewardIssue issue = RewardIssue.builder()
        .event(participation.getEvent())
        .user(participation.getUser())
        .participation(participation)
        .rewardPolicy(policy)
        .rewardDate(rewardDate)
        .build();

    return Optional.of(RewardIssueResponse.of(rewardIssueRepository.save(issue)));
  }

  private long determineParticipationOrder(RewardPolicy policy, EventParticipation participation) {
    // 현재는 일별 스코프만 지원
    return participationRepository.countByEvent_IdAndParticipationDateAndIdLessThanEqual(
        participation.getEvent().getId(), participation.getParticipationDate(), participation.getId());
  }

  private boolean hasWinnerForScope(RewardPolicy policy, LocalDate rewardDate) {
    return rewardIssueRepository.existsByRewardPolicy_IdAndRewardDate(policy.getId(), rewardDate);
  }

  private boolean exceedsUserPolicyLimit(RewardPolicy policy, Long userId, LocalDate rewardDate) {
    Integer totalLimit = policy.getUserLimitTotal();
    if (totalLimit != null && totalLimit > 0) {
      long totalCount = rewardIssueRepository.countByRewardPolicy_IdAndUser_Id(policy.getId(), userId);
      if (totalCount >= totalLimit) {
        return true;
      }
    }

    Integer dailyLimit = policy.getUserLimitPerDay();
    if (dailyLimit != null && dailyLimit > 0) {
      long dailyCount = rewardIssueRepository.countByRewardPolicy_IdAndUser_IdAndRewardDate(
          policy.getId(), userId, rewardDate);
      if (dailyCount >= dailyLimit) {
        return true;
      }
    }

    return false;
  }

  @Override
  public void downloadExcel(Long eventId, Instant start, Instant end, HttpServletResponse response) {
    validatePeriod(start, end);
    List<RewardIssueExcelRow> rows = rewardIssueRepository.findExcelRows(eventId, start, end);

    List<List<String>> body = rows.stream()
        .map(row -> List.of(
            StringUtil.formatInstant(row.rewardIssuedDt()),
            StringUtil.formatLocalDate(row.rewardDate()),
            StringUtil.defaultString(row.userName()),
            StringUtil.defaultString(row.employeeNumber()),
            StringUtil.defaultString(row.policyName())))
        .toList();

    ExcelUtil.downloadXlsx(
        response,
        "reward-issues.xlsx",
        ISSUE_HEADERS,
        body);
  }

  private void validatePeriod(Instant start, Instant end) {
    if (start == null || end == null || start.isAfter(end)) {
      throw new IllegalArgumentException("유효한 기간이 필요합니다.");
    }
  }
}
