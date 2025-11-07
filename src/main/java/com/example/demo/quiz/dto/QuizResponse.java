package com.example.demo.quiz.dto;

import com.example.demo.quiz.domain.Quiz;
import com.example.demo.quiz.domain.QuizType;
import java.time.Instant;
import java.time.LocalDate;

public record QuizResponse(
    Long id,
    Long eventId,
    QuizType type,
    String questionText,
    String correctText,
    LocalDate quizDate,
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
        quiz.getQuizDate(),
        quiz.getQuestionOrder(),
        quiz.isActive(),
        quiz.getCreatedDt());
  }
}
