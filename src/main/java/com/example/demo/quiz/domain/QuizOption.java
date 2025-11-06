package com.example.demo.quiz.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizOption {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Quiz quiz;

  @Column(length = 10, nullable = false)
  private String optionKey;

  @Column(length = 500, nullable = false)
  private String optionText;

  @Column(nullable = false)
  private boolean correct;

  @Column(nullable = false)
  private int optionOrder;

  @Builder
  private QuizOption(
      final Quiz quiz,
      final String optionKey,
      final String optionText,
      final boolean correct,
      final int optionOrder) {
    this.quiz = quiz;
    this.optionKey = optionKey;
    this.optionText = optionText;
    this.correct = correct;
    this.optionOrder = optionOrder;
  }

  public void markAsCorrect() {
    this.correct = true;
  }

  public void markAsIncorrect() {
    this.correct = false;
  }
}
