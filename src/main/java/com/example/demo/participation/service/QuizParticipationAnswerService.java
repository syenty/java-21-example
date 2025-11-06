package com.example.demo.participation.service;

import com.example.demo.participation.dto.QuizParticipationAnswerRequest;
import com.example.demo.participation.dto.QuizParticipationAnswerResponse;
import java.util.List;
import java.util.Optional;

public interface QuizParticipationAnswerService {
  List<QuizParticipationAnswerResponse> findAll();

  Optional<QuizParticipationAnswerResponse> findById(Long id);

  Optional<QuizParticipationAnswerResponse> create(QuizParticipationAnswerRequest request);

  Optional<QuizParticipationAnswerResponse> update(
      Long id, QuizParticipationAnswerRequest request);

  boolean delete(Long id);
}
