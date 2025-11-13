package com.example.demo.quiz.dto;

import java.time.Instant;
import java.time.LocalDate;

public record QuizLookupResponse(
    Long id,
    Long eventId,
    String eventName,
    String type,
    String questionText,
    String correctText,
    LocalDate quizDate,
    int questionOrder,
    boolean active,
    Instant createdDt) {}
