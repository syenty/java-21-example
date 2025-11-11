package com.example.demo.quiz.dto;

import com.example.demo.quiz.domain.Quiz;
import com.example.demo.quiz.domain.QuizType;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public record QuizAdminResponse(
    Long id,
    Long eventId,
    QuizType type,
    String questionText,
    String correctText,
    LocalDate quizDate,
    int questionOrder,
    boolean active,
    Instant createdDt,
    List<QuizOptionResponse> options) {

  public QuizAdminResponse {
    options = options == null ? List.of() : List.copyOf(options);
  }

  public static QuizAdminResponse of(Quiz quiz) {
    return of(quiz, List.of());
  }

  public static QuizAdminResponse of(Quiz quiz, List<QuizOptionResponse> options) {
    return new QuizAdminResponse(
        quiz.getId(),
        quiz.getEvent().getId(),
        quiz.getType(),
        quiz.getQuestionText(),
        quiz.getCorrectText(),
        quiz.getQuizDate(),
        quiz.getQuestionOrder(),
        quiz.isActive(),
        quiz.getCreatedDt(),
        options);
  }
}
