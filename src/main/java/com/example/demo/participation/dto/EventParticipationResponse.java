package com.example.demo.participation.dto;

import com.example.demo.participation.domain.EventParticipation;
import java.time.Instant;
import java.time.LocalDate;

public record EventParticipationResponse(
    Long id,
    Long eventId,
    Long userId,
    Instant participationDt,
    LocalDate participationDate,
    int dailyOrder,
    boolean correct,
    int correctCount,
    int totalQuestions) {

  public static EventParticipationResponse of(EventParticipation participation) {
    return new EventParticipationResponse(
        participation.getId(),
        participation.getEvent().getId(),
        participation.getUser().getId(),
        participation.getParticipationDt(),
        participation.getParticipationDate(),
        participation.getDailyOrder(),
        participation.isCorrect(),
        participation.getCorrectCount(),
        participation.getTotalQuestions());
  }
}
