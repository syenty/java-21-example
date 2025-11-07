package com.example.demo.quiz.service;

import com.example.demo.participation.dto.QuizParticipationRequest;
import com.example.demo.quiz.dto.QuizRequest;
import com.example.demo.quiz.dto.QuizResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface QuizService {
  List<QuizResponse> findAll();

  Optional<QuizResponse> findById(Long id);

  Optional<QuizResponse> create(QuizRequest request);

  Optional<QuizResponse> update(Long id, QuizRequest request);

  boolean delete(Long id);

  List<QuizResponse> findByEventAndDate(Long eventId, LocalDate quizDate);

  boolean isCorrectAnswer(Long quizId, Long eventId, String answer);

  boolean areAllAnswersCorrect(Long eventId, List<QuizParticipationRequest.QuizAnswer> answers);
}
