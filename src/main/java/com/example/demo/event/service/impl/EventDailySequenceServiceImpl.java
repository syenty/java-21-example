package com.example.demo.event.service.impl;

import com.example.demo.event.domain.Event;
import com.example.demo.event.domain.EventDailySequence;
import com.example.demo.event.domain.EventDailySequenceId;
import com.example.demo.event.dto.EventDailySequenceRequest;
import com.example.demo.event.dto.EventDailySequenceResponse;
import com.example.demo.event.repository.EventDailySequenceRepository;
import com.example.demo.event.repository.EventRepository;
import com.example.demo.event.service.EventDailySequenceService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventDailySequenceServiceImpl implements EventDailySequenceService {

  private final EventDailySequenceRepository sequenceRepository;
  private final EventRepository eventRepository;

  @Override
  public List<EventDailySequenceResponse> findByEvent(Long eventId) {
    return sequenceRepository.findByEvent_Id(eventId).stream()
        .map(EventDailySequenceResponse::of)
        .toList();
  }

  @Override
  public Optional<EventDailySequenceResponse> findById(Long eventId, LocalDate seqDate) {
    return sequenceRepository
        .findById(new EventDailySequenceId(eventId, seqDate))
        .map(EventDailySequenceResponse::of);
  }

  @Override
  @Transactional
  public Optional<EventDailySequenceResponse> create(EventDailySequenceRequest request) {
    Optional<Event> eventOpt = eventRepository.findById(request.eventId());
    if (eventOpt.isEmpty()) {
      return Optional.empty();
    }
    EventDailySequence sequence =
        EventDailySequence.builder()
            .event(eventOpt.get())
            .id(new EventDailySequenceId(request.eventId(), request.seqDate()))
            .lastSequence(request.lastSequence())
            .build();
    return Optional.of(EventDailySequenceResponse.of(sequenceRepository.save(sequence)));
  }

  @Override
  @Transactional
  public Optional<EventDailySequenceResponse> update(
      Long eventId, LocalDate seqDate, EventDailySequenceRequest request) {
    return sequenceRepository
        .findById(new EventDailySequenceId(eventId, seqDate))
        .map(
            sequence -> {
              sequence.updateLastSequence(request.lastSequence());
              return EventDailySequenceResponse.of(sequence);
            });
  }

  @Override
  @Transactional
  public boolean delete(Long eventId, LocalDate seqDate) {
    EventDailySequenceId id = new EventDailySequenceId(eventId, seqDate);
    if (!sequenceRepository.existsById(id)) {
      return false;
    }
    sequenceRepository.deleteById(id);
    return true;
  }
}
