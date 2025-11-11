package com.example.demo.reward.service.impl;

import com.example.demo.event.domain.Event;
import com.example.demo.event.repository.EventRepository;
import com.example.demo.participation.domain.QuizParticipation;
import com.example.demo.participation.repository.QuizParticipationRepository;
import com.example.demo.reward.domain.RewardIssue;
import com.example.demo.reward.domain.RewardPolicy;
import com.example.demo.reward.domain.RewardPolicyType;
import com.example.demo.reward.dto.RewardIssueRequest;
import com.example.demo.reward.dto.RewardIssueResponse;
import com.example.demo.reward.repository.RewardIssueRepository;
import com.example.demo.reward.repository.RewardPolicyRepository;
import com.example.demo.reward.service.RewardIssueService;
import com.example.demo.user.domain.User;
import com.example.demo.user.repository.UserRepository;
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
  private final QuizParticipationRepository participationRepository;
  private final RewardPolicyRepository rewardPolicyRepository;

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
    Optional<QuizParticipation> participationOpt = participationRepository.findById(request.participationId());
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
                Optional<QuizParticipation> participationOpt = participationRepository
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
      QuizParticipation participation,
      LocalDate rewardDate) {
    if (policies == null || policies.isEmpty()) {
      return Optional.empty();
    }

    Event event = participation.getEvent();
    if (hasReachedEventRewardLimit(event, participation.getUser().getId())) {
      return Optional.empty();
    }

    for (RewardPolicy policy : policies) {
      if (policy.getPolicyType() == RewardPolicyType.NTH_ORDER) {
        Optional<RewardIssueResponse> issued = issueNthOrder(policy, participation, rewardDate);
        if (issued.isPresent()) {
          return issued;
        }
      }
    }
    return Optional.empty();
  }

  private boolean hasReachedEventRewardLimit(Event event, Long userId) {
    Integer limit = event.getRewardLimitPerUser();
    if (limit == null || limit <= 0) {
      return false;
    }
    long issuedCount =
        rewardIssueRepository.countByEvent_IdAndUser_Id(event.getId(), userId);
    return issuedCount >= limit;
  }

  private Optional<RewardIssueResponse> issueNthOrder(RewardPolicy policy,
      QuizParticipation participation, LocalDate rewardDate) {
    Integer targetOrder = policy.getTargetOrder();
    if (targetOrder == null) {
      return Optional.empty();
    }

    long order = participationRepository.countByEvent_IdAndParticipationDateAndIdLessThanEqual(
        participation.getEvent().getId(), participation.getParticipationDate(), participation.getId());
    if (order != targetOrder) {
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
}
