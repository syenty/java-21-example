package com.example.demo.participation.service;

import com.example.demo.participation.dto.EventParticipationRequest;
import com.example.demo.participation.dto.EventParticipationResponse;
import java.util.List;
import java.util.Optional;

public interface EventParticipationService {
  List<EventParticipationResponse> findAll();

  Optional<EventParticipationResponse> findById(Long id);

  Optional<EventParticipationResponse> create(EventParticipationRequest request);

  Optional<EventParticipationResponse> update(Long id, EventParticipationRequest request);

  boolean delete(Long id);
}
