package com.example.demo.participation.dto;

import java.util.List;

public record QuizParticipationRequest(Long eventId, Long userId, List<QuizAnswer> answers) {

  public record QuizAnswer(Long quizId, Long optionId, String answerText) {}
}
