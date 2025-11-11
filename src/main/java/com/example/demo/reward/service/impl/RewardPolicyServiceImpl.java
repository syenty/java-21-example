package com.example.demo.reward.service.impl;

import com.example.demo.event.repository.EventRepository;
import com.example.demo.reward.domain.RewardPolicy;
import com.example.demo.reward.domain.RewardPolicyNthScope;
import com.example.demo.reward.domain.RewardPolicyType;
import com.example.demo.reward.dto.RewardPolicyRequest;
import com.example.demo.reward.dto.RewardPolicyResponse;
import com.example.demo.reward.repository.RewardPolicyRepository;
import com.example.demo.reward.service.RewardPolicyService;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
    Integer targetOrder = request.targetOrder();
    RewardPolicyNthScope nthScope = request.nthScope();
    validatePolicyRequirements(request.policyType(), targetOrder, nthScope);

    if (request.policyType() == RewardPolicyType.FIRST_COME) {
      targetOrder = null;
      nthScope = null;
    }
    final Integer finalTargetOrder = targetOrder;
    final RewardPolicyNthScope finalNthScope = nthScope;
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
                      .targetOrder(finalTargetOrder)
                      .nthScope(finalNthScope)
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
              RewardPolicyType policyTypeToApply =
                  request.policyType() != null ? request.policyType() : policy.getPolicyType();
              Integer targetOrder = resolveTargetOrder(policy, request, policyTypeToApply);
              RewardPolicyNthScope nthScope = resolveNthScope(policy, request, policyTypeToApply);
              validatePolicyRequirements(policyTypeToApply, targetOrder, nthScope);

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
                              targetOrder,
                              nthScope,
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
                  targetOrder,
                  nthScope,
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

  @Override
  public List<RewardPolicy> findActivePolicies(Long eventId, Instant utcNow) {
    LocalDateTime now = LocalDateTime.ofInstant(utcNow, ZoneOffset.UTC);
    return rewardPolicyRepository.findByEvent_IdAndStartDtLessThanEqualAndEndDtGreaterThanEqual(
        eventId, now, now);
  }

  private Integer resolveTargetOrder(
      RewardPolicy policy, RewardPolicyRequest request, RewardPolicyType policyType) {
    if (policyType == RewardPolicyType.FIRST_COME) {
      return null;
    }
    if (request.targetOrder() != null) {
      return request.targetOrder();
    }
    return policy.getTargetOrder();
  }

  private RewardPolicyNthScope resolveNthScope(
      RewardPolicy policy, RewardPolicyRequest request, RewardPolicyType policyType) {
    if (policyType == RewardPolicyType.FIRST_COME) {
      return null;
    }
    if (request.nthScope() != null) {
      return request.nthScope();
    }
    return policy.getNthScope();
  }

  private void validatePolicyRequirements(
      RewardPolicyType policyType, Integer targetOrder, RewardPolicyNthScope nthScope) {
    if (policyType == null) {
      throw new IllegalArgumentException("policyType must be provided");
    }
    if (policyType == RewardPolicyType.NTH_ORDER) {
      if (targetOrder == null || targetOrder <= 0) {
        throw new IllegalArgumentException("targetOrder must be a positive integer");
      }
      if (nthScope == null) {
        throw new IllegalArgumentException("nthScope is required for NTH_ORDER policies");
      }
    }
  }
}
