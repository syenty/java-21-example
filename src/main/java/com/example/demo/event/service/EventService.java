package com.example.demo.event.service;

import com.example.demo.event.dto.EventRequest;
import com.example.demo.event.dto.EventResponse;
import java.util.List;
import java.util.Optional;

public interface EventService {
  List<EventResponse> findAll();

  Optional<EventResponse> findById(Long id);

  EventResponse create(EventRequest request);

  Optional<EventResponse> update(Long id, EventRequest request);

  boolean delete(Long id);
}
