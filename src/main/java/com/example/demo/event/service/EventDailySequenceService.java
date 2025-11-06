package com.example.demo.event.service;

import com.example.demo.event.dto.EventDailySequenceRequest;
import com.example.demo.event.dto.EventDailySequenceResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EventDailySequenceService {
  List<EventDailySequenceResponse> findByEvent(Long eventId);

  Optional<EventDailySequenceResponse> findById(Long eventId, LocalDate seqDate);

  Optional<EventDailySequenceResponse> create(EventDailySequenceRequest request);

  Optional<EventDailySequenceResponse> update(
      Long eventId, LocalDate seqDate, EventDailySequenceRequest request);

  boolean delete(Long eventId, LocalDate seqDate);
}
