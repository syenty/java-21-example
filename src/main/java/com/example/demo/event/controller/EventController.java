package com.example.demo.event.controller;

import com.example.demo.event.dto.EventRequest;
import com.example.demo.event.dto.EventResponse;
import com.example.demo.event.service.EventService;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

  private final EventService eventService;

  @GetMapping
  public List<EventResponse> getAll() {
    return eventService.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<EventResponse> getById(@PathVariable Long id) {
    return eventService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<EventResponse> create(@RequestBody EventRequest request) {
    EventResponse created = eventService.create(request);
    return ResponseEntity.created(URI.create("/api/events/" + created.id())).body(created);
  }

  @PutMapping("/{id}")
  public ResponseEntity<EventResponse> update(
      @PathVariable Long id, @RequestBody EventRequest request) {
    return eventService.update(id, request).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    return eventService.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
  }
}
