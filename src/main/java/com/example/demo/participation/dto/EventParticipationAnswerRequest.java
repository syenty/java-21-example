package com.example.demo.participation.dto;

import java.time.Instant;

public record EventParticipationAnswerRequest(
    Long participationId,
    Long quizId,
    Long optionId,
    String answerText,
    Boolean correct,
    Instant answeredDt) {}
