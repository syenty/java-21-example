package com.example.demo.quiz.dto;

import com.example.demo.quiz.domain.QuizOption;

public record QuizOptionResponse(
    Long id, Long quizId, String optionKey, String optionText, boolean correct, int optionOrder) {

  public static QuizOptionResponse of(QuizOption option) {
    return new QuizOptionResponse(
        option.getId(),
        option.getQuiz().getId(),
        option.getOptionKey(),
        option.getOptionText(),
        option.isCorrect(),
        option.getOptionOrder());
  }
}
