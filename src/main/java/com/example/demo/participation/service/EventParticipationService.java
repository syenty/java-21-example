package com.example.demo.participation.service;

import com.example.demo.participation.dto.EventParticipationRequest;
import com.example.demo.participation.dto.EventParticipationResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface EventParticipationService {
  List<EventParticipationResponse> findAll();

  Optional<EventParticipationResponse> findById(Long id);

  Optional<EventParticipationResponse> create(EventParticipationRequest request);

  Optional<EventParticipationResponse> update(Long id, EventParticipationRequest request);

  boolean delete(Long id);

  List<EventParticipationResponse> findByEventAndPeriod(Long eventId, Instant start, Instant end);

  void downloadExcel(Long eventId, Instant start, Instant end, HttpServletResponse response);
}
