package com.example.demo.quiz.domain;

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
public class Quiz {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Event event;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private QuizType type;

  @Column(columnDefinition = "TEXT", nullable = false)
  private String questionText;

  @Column(length = 500)
  private String correctText;

  @Column(nullable = false)
  private LocalDate quizDate;

  @Column(nullable = false)
  private int questionOrder;

  @Column(nullable = false)
  private boolean active;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private Instant createdDt;

  @Builder
  private Quiz(
      final Event event,
      final QuizType type,
      final String questionText,
      final String correctText,
      final LocalDate quizDate,
      final int questionOrder,
      final boolean active) {
    this.event = event;
    this.type = type;
    this.questionText = questionText;
    this.correctText = correctText;
    this.quizDate = quizDate;
    this.questionOrder = questionOrder;
    this.active = active;
  }

  public void activate() {
    this.active = true;
  }

  public void deactivate() {
    this.active = false;
  }

  public void changeEvent(Event event) {
    this.event = event;
  }

  public void update(
      QuizType type,
      String questionText,
      String correctText,
      LocalDate quizDate,
      Integer questionOrder,
      Boolean active) {
    if (type != null) {
      this.type = type;
    }
    this.questionText = questionText;
    this.correctText = correctText;
    if (quizDate != null) {
      this.quizDate = quizDate;
    }
    if (questionOrder != null) {
      this.questionOrder = questionOrder;
    }
    if (active != null) {
      this.active = active;
    }
  }
}
