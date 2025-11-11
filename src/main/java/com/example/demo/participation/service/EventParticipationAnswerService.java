package com.example.demo.participation.service;

import com.example.demo.participation.dto.EventParticipationAnswerRequest;
import com.example.demo.participation.dto.EventParticipationAnswerResponse;
import java.util.List;
import java.util.Optional;

public interface EventParticipationAnswerService {
  List<EventParticipationAnswerResponse> findAll();

  Optional<EventParticipationAnswerResponse> findById(Long id);

  Optional<EventParticipationAnswerResponse> create(EventParticipationAnswerRequest request);

  Optional<EventParticipationAnswerResponse> update(
      Long id, EventParticipationAnswerRequest request);

  boolean delete(Long id);
}
