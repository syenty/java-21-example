package com.example.demo.participation.service;

import com.example.demo.participation.dto.QuizParticipationRequest;
import com.example.demo.participation.dto.QuizParticipationResponse;
import java.util.List;
import java.util.Optional;

public interface QuizParticipationService {
  List<QuizParticipationResponse> findAll();

  Optional<QuizParticipationResponse> findById(Long id);

  Optional<QuizParticipationResponse> create(QuizParticipationRequest request);

  Optional<QuizParticipationResponse> update(Long id, QuizParticipationRequest request);

  boolean delete(Long id);
}
