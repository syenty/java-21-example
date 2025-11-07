package com.example.demo.quiz.dto;

import com.example.demo.quiz.domain.QuizType;
import java.time.LocalDate;

public record QuizRequest(
    Long eventId,
    QuizType type,
    String questionText,
    String correctText,
    LocalDate quizDate,
    Integer questionOrder,
    Boolean active) {}
