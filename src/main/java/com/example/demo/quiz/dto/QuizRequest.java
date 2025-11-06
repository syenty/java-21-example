package com.example.demo.quiz.dto;

import com.example.demo.quiz.domain.QuizType;

public record QuizRequest(
    Long eventId,
    QuizType type,
    String questionText,
    String correctText,
    Integer questionOrder,
    Boolean active) {}
