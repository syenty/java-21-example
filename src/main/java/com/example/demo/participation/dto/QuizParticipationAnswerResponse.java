package com.example.demo.participation.dto;

import com.example.demo.participation.domain.QuizParticipationAnswer;
import java.time.Instant;

public record QuizParticipationAnswerResponse(
    Long id,
    Long participationId,
    Long quizId,
    Long optionId,
    String answerText,
    boolean correct,
    Instant answeredDt) {

  public static QuizParticipationAnswerResponse of(QuizParticipationAnswer answer) {
    return new QuizParticipationAnswerResponse(
        answer.getId(),
        answer.getParticipation().getId(),
        answer.getQuiz().getId(),
        answer.getOption() != null ? answer.getOption().getId() : null,
        answer.getAnswerText(),
        answer.isCorrect(),
        answer.getAnsweredDt());
  }
}
