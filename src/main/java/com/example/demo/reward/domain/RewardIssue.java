package com.example.demo.reward.domain;

import com.example.demo.event.domain.Event;
import com.example.demo.participation.domain.QuizParticipation;
import com.example.demo.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RewardIssue {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Event event;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private QuizParticipation participation;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private RewardPolicy rewardPolicy;

  @Column(nullable = false)
  private LocalDate rewardDate;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private Instant issuedDt;

  @Builder
  private RewardIssue(
      final Event event,
      final User user,
      final QuizParticipation participation,
      final RewardPolicy rewardPolicy,
      final LocalDate rewardDate) {
    this.event = event;
    this.user = user;
    this.participation = participation;
    this.rewardPolicy = rewardPolicy;
    this.rewardDate = rewardDate;
  }
}
