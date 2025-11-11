package com.example.demo.participation.domain;

import com.example.demo.event.domain.Event;
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

@Getter
@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizParticipation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Event event;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private User user;

  @Column(nullable = false)
  private Instant participationDt;

  @Column(nullable = false)
  private LocalDate participationDate;

  @Column(nullable = false)
  private int dailyOrder;

  @Column(nullable = false)
  private boolean correct;

  @Column(nullable = false)
  private int correctCount;

  @Column(nullable = false)
  private int totalQuestions;

  @Builder
  private QuizParticipation(
      final Event event,
      final User user,
      final Instant participationDt,
      final LocalDate participationDate,
      final int dailyOrder,
      final boolean correct,
      final int correctCount,
      final int totalQuestions) {
    this.event = event;
    this.user = user;
    this.participationDt = participationDt;
    this.participationDate = participationDate;
    this.dailyOrder = dailyOrder;
    this.correct = correct;
    this.correctCount = correctCount;
    this.totalQuestions = totalQuestions;
  }

  public void changeEvent(Event event) {
    this.event = event;
  }

  public void changeUser(User user) {
    this.user = user;
  }

  public void update(
      Instant participationDt,
      LocalDate participationDate,
      Integer dailyOrder,
      Boolean correct,
      Integer correctCount,
      Integer totalQuestions) {
    if (participationDt != null) {
      this.participationDt = participationDt;
    }
    if (participationDate != null) {
      this.participationDate = participationDate;
    }
    if (dailyOrder != null) {
      this.dailyOrder = dailyOrder;
    }
    if (correct != null) {
      this.correct = correct;
    }
    if (correctCount != null) {
      this.correctCount = correctCount;
    }
    if (totalQuestions != null) {
      this.totalQuestions = totalQuestions;
    }
  }
}
