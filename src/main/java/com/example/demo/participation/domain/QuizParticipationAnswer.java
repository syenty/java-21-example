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
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizParticipationAnswer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private QuizParticipation participation;

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
  private QuizParticipationAnswer(
      final QuizParticipation participation,
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
}
