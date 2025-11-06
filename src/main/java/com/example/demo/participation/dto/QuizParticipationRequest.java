package com.example.demo.participation.dto;

import java.time.Instant;
import java.time.LocalDate;

public record QuizParticipationRequest(
    Long eventId,
    Long userId,
    Instant participationDt,
    LocalDate participationDate,
    Integer dailyOrder,
    Boolean correct,
    Integer score,
    Integer correctCount,
    Integer totalQuestions) {}
