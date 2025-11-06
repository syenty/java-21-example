package com.example.demo.event.service.impl;

import com.example.demo.event.domain.Event;
import com.example.demo.event.dto.EventRequest;
import com.example.demo.event.dto.EventResponse;
import com.example.demo.event.repository.EventRepository;
import com.example.demo.event.service.EventService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

  private final EventRepository eventRepository;

  @Override
  public List<EventResponse> findAll() {
    return eventRepository.findAll().stream().map(EventResponse::of).toList();
  }

  @Override
  public Optional<EventResponse> findById(Long id) {
    return eventRepository.findById(id).map(EventResponse::of);
  }

  @Override
  @Transactional
  public EventResponse create(EventRequest request) {
    Event event =
        Event.builder()
            .name(request.name())
            .description(request.description())
            .startDt(request.startDt())
            .endDt(request.endDt())
            .maxDailyTry(
                request.maxDailyTry() != null && request.maxDailyTry() > 0
                    ? request.maxDailyTry()
                    : 1)
            .status(request.status())
            .build();

    Event saved = eventRepository.save(event);
    return EventResponse.of(saved);
  }

  @Override
  @Transactional
  public Optional<EventResponse> update(Long id, EventRequest request) {
    return eventRepository
        .findById(id)
        .map(
            event -> {
              event.updateDetail(
                  request.name(),
                  request.description(),
                  request.startDt(),
                  request.endDt(),
                  request.maxDailyTry(),
                  request.status());
              return EventResponse.of(event);
            });
  }

  @Override
  @Transactional
  public boolean delete(Long id) {
    if (!eventRepository.existsById(id)) {
      return false;
    }
    eventRepository.deleteById(id);
    return true;
  }
}
