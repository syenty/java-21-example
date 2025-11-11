package com.example.demo.quiz.dto;

import com.example.demo.quiz.domain.Quiz;
import com.example.demo.quiz.domain.QuizType;
import java.time.LocalDate;
import java.util.List;

public record QuizUserResponse(
    Long id,
    Long eventId,
    QuizType type,
    String questionText,
    LocalDate quizDate,
    int questionOrder,
    List<QuizOptionUserResponse> options) {

  public QuizUserResponse {
    options = options == null ? List.of() : List.copyOf(options);
  }

  public static QuizUserResponse of(Quiz quiz, List<QuizOptionUserResponse> options) {
    return new QuizUserResponse(
        quiz.getId(),
        quiz.getEvent().getId(),
        quiz.getType(),
        quiz.getQuestionText(),
        quiz.getQuizDate(),
        quiz.getQuestionOrder(),
        options);
  }
}
