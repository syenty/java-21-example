package com.example.demo.quiz.service;

import com.example.demo.participation.dto.EventParticipationRequest;
import com.example.demo.quiz.dto.QuizAdminResponse;
import com.example.demo.quiz.dto.QuizRequest;
import com.example.demo.quiz.dto.QuizUserResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface QuizService {
  List<QuizAdminResponse> findAll();

  Optional<QuizAdminResponse> findById(Long id);

  Optional<QuizAdminResponse> create(QuizRequest request);

  Optional<QuizAdminResponse> update(Long id, QuizRequest request);

  boolean delete(Long id);

  List<QuizUserResponse> findByEventAndDate(Long eventId, LocalDate quizDate);

  List<QuizUserResponse> findTodayByEvent(Long eventId);

  boolean isCorrectAnswer(Long quizId, Long eventId, String answer);

  boolean areAllAnswersCorrect(Long eventId, List<EventParticipationRequest.QuizAnswer> answers);

  int nextQuestionOrder(Long eventId, LocalDate quizDate);
}
