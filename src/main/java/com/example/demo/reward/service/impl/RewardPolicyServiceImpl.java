package com.example.demo.reward.service.impl;

import com.example.demo.event.repository.EventRepository;
import com.example.demo.reward.domain.RewardPolicy;
import com.example.demo.reward.dto.RewardPolicyRequest;
import com.example.demo.reward.dto.RewardPolicyResponse;
import com.example.demo.reward.repository.RewardPolicyRepository;
import com.example.demo.reward.service.RewardPolicyService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RewardPolicyServiceImpl implements RewardPolicyService {

  private final RewardPolicyRepository rewardPolicyRepository;
  private final EventRepository eventRepository;

  @Override
  public List<RewardPolicyResponse> findAll() {
    return rewardPolicyRepository.findAll().stream().map(RewardPolicyResponse::of).toList();
  }

  @Override
  public Optional<RewardPolicyResponse> findById(Long id) {
    return rewardPolicyRepository.findById(id).map(RewardPolicyResponse::of);
  }

  @Override
  @Transactional
  public Optional<RewardPolicyResponse> create(RewardPolicyRequest request) {
    return eventRepository
        .findById(request.eventId())
        .map(
            event -> {
              RewardPolicy policy =
                  RewardPolicy.builder()
                      .event(event)
                      .name(request.name())
                      .policyType(request.policyType())
                      .startDt(request.startDt())
                      .endDt(request.endDt())
                      .winnerLimitTotal(request.winnerLimitTotal())
                      .winnerLimitPerDay(request.winnerLimitPerDay())
                      .targetOrder(request.targetOrder())
                      .nthScope(request.nthScope())
                      .userLimitTotal(request.userLimitTotal())
                      .userLimitPerDay(request.userLimitPerDay())
                      .rewardType(request.rewardType())
                      .rewardValue(request.rewardValue())
                      .build();
              return RewardPolicyResponse.of(rewardPolicyRepository.save(policy));
            });
  }

  @Override
  @Transactional
  public Optional<RewardPolicyResponse> update(Long id, RewardPolicyRequest request) {
    return rewardPolicyRepository
        .findById(id)
        .flatMap(
            policy -> {
              if (request.eventId() != null
                  && !policy.getEvent().getId().equals(request.eventId())) {
                return eventRepository
                    .findById(request.eventId())
                    .map(
                        event -> {
                          policy.changeEvent(event);
                          policy.update(
                              request.name(),
                              request.policyType(),
                              request.startDt(),
                              request.endDt(),
                              request.winnerLimitTotal(),
                              request.winnerLimitPerDay(),
                              request.targetOrder(),
                              request.nthScope(),
                              request.userLimitTotal(),
                              request.userLimitPerDay(),
                              request.rewardType(),
                              request.rewardValue());
                          return RewardPolicyResponse.of(policy);
                        });
              }
              policy.update(
                  request.name(),
                  request.policyType(),
                  request.startDt(),
                  request.endDt(),
                  request.winnerLimitTotal(),
                  request.winnerLimitPerDay(),
                  request.targetOrder(),
                  request.nthScope(),
                  request.userLimitTotal(),
                  request.userLimitPerDay(),
                  request.rewardType(),
                  request.rewardValue());
              return Optional.of(RewardPolicyResponse.of(policy));
            });
  }

  @Override
  @Transactional
  public boolean delete(Long id) {
    if (!rewardPolicyRepository.existsById(id)) {
      return false;
    }
    rewardPolicyRepository.deleteById(id);
    return true;
  }
}
