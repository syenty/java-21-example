package com.example.demo.quiz.dto;

public record QuizOptionRequest(
    Long quizId, String optionKey, String optionText, Boolean correct, Integer optionOrder) {}
