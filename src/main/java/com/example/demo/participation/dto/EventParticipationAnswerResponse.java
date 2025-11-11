package com.example.demo.participation.dto;

import com.example.demo.participation.domain.EventParticipationAnswer;
import java.time.Instant;

public record EventParticipationAnswerResponse(
    Long id,
    Long participationId,
    Long quizId,
    Long optionId,
    String answerText,
    boolean correct,
    Instant answeredDt) {

  public static EventParticipationAnswerResponse of(EventParticipationAnswer answer) {
    return new EventParticipationAnswerResponse(
        answer.getId(),
        answer.getParticipation().getId(),
        answer.getQuiz().getId(),
        answer.getOption() != null ? answer.getOption().getId() : null,
        answer.getAnswerText(),
        answer.isCorrect(),
        answer.getAnsweredDt());
  }
}
