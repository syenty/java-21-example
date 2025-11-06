package com.example.demo.participation.dto;

import com.example.demo.participation.domain.QuizParticipation;
import java.time.Instant;
import java.time.LocalDate;

public record QuizParticipationResponse(
    Long id,
    Long eventId,
    Long userId,
    Instant participationDt,
    LocalDate participationDate,
    int dailyOrder,
    boolean correct,
    int score,
    int correctCount,
    int totalQuestions) {

  public static QuizParticipationResponse of(QuizParticipation participation) {
    return new QuizParticipationResponse(
        participation.getId(),
        participation.getEvent().getId(),
        participation.getUser().getId(),
        participation.getParticipationDt(),
        participation.getParticipationDate(),
        participation.getDailyOrder(),
        participation.isCorrect(),
        participation.getScore(),
        participation.getCorrectCount(),
        participation.getTotalQuestions());
  }
}
