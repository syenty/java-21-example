package com.example.demo.participation.domain;

import com.example.demo.quiz.domain.Quiz;
import com.example.demo.quiz.domain.QuizOption;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.Instant;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "event_participation_answer", uniqueConstraints = @UniqueConstraint(name = "uk_epa_participation_quiz", columnNames = {
    "participation_id", "quiz_id" }))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventParticipationAnswer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private EventParticipation participation;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Quiz quiz;

  @ManyToOne(fetch = FetchType.LAZY)
  private QuizOption option;

  @Column(length = 500)
  private String answerText;

  @Column(nullable = false)
  private boolean correct;

  @Column(nullable = false)
  private Instant answeredDt;

  @Builder
  private EventParticipationAnswer(
      final EventParticipation participation,
      final Quiz quiz,
      final QuizOption option,
      final String answerText,
      final boolean correct,
      final Instant answeredDt) {
    this.participation = participation;
    this.quiz = quiz;
    this.option = option;
    this.answerText = answerText;
    this.correct = correct;
    this.answeredDt = answeredDt;
  }

  public void update(String answerText, Boolean correct, Instant answeredDt, QuizOption option) {
    this.answerText = answerText;
    if (correct != null) {
      this.correct = correct;
    }
    if (answeredDt != null) {
      this.answeredDt = answeredDt;
    }
    this.option = option;
  }

  public void changeParticipation(EventParticipation participation) {
    this.participation = participation;
  }

  public void changeQuiz(Quiz quiz) {
    this.quiz = quiz;
  }
}
