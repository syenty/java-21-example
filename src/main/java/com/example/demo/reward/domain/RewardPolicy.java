package com.example.demo.reward.domain;

import com.example.demo.common.domain.BaseTimeEntity;
import com.example.demo.event.domain.Event;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RewardPolicy extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Event event;

  @Column(length = 200, nullable = false)
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private RewardPolicyType policyType;

  @Column(nullable = false)
  private LocalDateTime startDt;

  @Column(nullable = false)
  private LocalDateTime endDt;

  @Column
  private Integer winnerLimitTotal;

  @Column
  private Integer winnerLimitPerDay;

  @Column
  private Integer targetOrder;

  @Enumerated(EnumType.STRING)
  @Column
  private RewardPolicyNthScope nthScope;

  @Column
  private Integer userLimitTotal;

  @Column
  private Integer userLimitPerDay;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private RewardType rewardType;

  @Column(length = 200, nullable = false)
  private String rewardValue;

  @Builder
  private RewardPolicy(
      final Event event,
      final String name,
      final RewardPolicyType policyType,
      final LocalDateTime startDt,
      final LocalDateTime endDt,
      final Integer winnerLimitTotal,
      final Integer winnerLimitPerDay,
      final Integer targetOrder,
      final RewardPolicyNthScope nthScope,
      final Integer userLimitTotal,
      final Integer userLimitPerDay,
      final RewardType rewardType,
      final String rewardValue) {
    this.event = event;
    this.name = name;
    this.policyType = policyType;
    this.startDt = startDt;
    this.endDt = endDt;
    this.winnerLimitTotal = winnerLimitTotal;
    this.winnerLimitPerDay = winnerLimitPerDay;
    this.targetOrder = targetOrder;
    this.nthScope = nthScope;
    this.userLimitTotal = userLimitTotal;
    this.userLimitPerDay = userLimitPerDay;
    this.rewardType = rewardType;
    this.rewardValue = rewardValue;
  }
}
