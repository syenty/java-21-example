package com.example.demo.quiz.dto;

import com.example.demo.quiz.domain.Quiz;
import com.example.demo.quiz.domain.QuizType;
import java.time.Instant;

public record QuizResponse(
    Long id,
    Long eventId,
    QuizType type,
    String questionText,
    String correctText,
    int questionOrder,
    boolean active,
    Instant createdDt) {

  public static QuizResponse of(Quiz quiz) {
    return new QuizResponse(
        quiz.getId(),
        quiz.getEvent().getId(),
        quiz.getType(),
        quiz.getQuestionText(),
        quiz.getCorrectText(),
        quiz.getQuestionOrder(),
        quiz.isActive(),
        quiz.getCreatedDt());
  }
}
