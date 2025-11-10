package com.example.demo.quiz.dto;

import com.example.demo.quiz.domain.QuizOption;

public record QuizOptionUserResponse(
    Long id, Long quizId, String optionKey, String optionText, int optionOrder) {

  public static QuizOptionUserResponse of(QuizOption option) {
    return new QuizOptionUserResponse(
        option.getId(),
        option.getQuiz().getId(),
        option.getOptionKey(),
        option.getOptionText(),
        option.getOptionOrder());
  }
}
