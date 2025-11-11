package com.example.demo.event.controller;

import com.example.demo.event.dto.EventDailySequenceRequest;
import com.example.demo.event.dto.EventDailySequenceResponse;
import com.example.demo.event.service.EventDailySequenceService;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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
@RequestMapping("/api/events/{eventId}/sequences")
@RequiredArgsConstructor
public class EventDailySequenceController {

  private final EventDailySequenceService sequenceService;

  @GetMapping
  public List<EventDailySequenceResponse> findByEvent(@PathVariable Long eventId) {
    return sequenceService.findByEvent(eventId);
  }

  @GetMapping("/{seqDate}")
  public ResponseEntity<EventDailySequenceResponse> findOne(
      @PathVariable Long eventId,
      @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate seqDate) {
    return sequenceService.findById(eventId, seqDate).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<EventDailySequenceResponse> create(
      @PathVariable Long eventId, @RequestBody EventDailySequenceRequest request) {
    EventDailySequenceRequest normalized =
        new EventDailySequenceRequest(eventId, request.seqDate(), request.lastSeq());
    return sequenceService
        .create(normalized)
        .map(
            response ->
                ResponseEntity.created(
                        URI.create("/api/events/" + eventId + "/sequences/" + response.seqDate()))
                    .body(response))
        .orElse(ResponseEntity.notFound().build());
  }

  @PutMapping("/{seqDate}")
  public ResponseEntity<EventDailySequenceResponse> update(
      @PathVariable Long eventId,
      @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate seqDate,
      @RequestBody EventDailySequenceRequest request) {
    return sequenceService
        .update(eventId, seqDate, request)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{seqDate}")
  public ResponseEntity<Void> delete(
      @PathVariable Long eventId,
      @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate seqDate) {
    return sequenceService.delete(eventId, seqDate)
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }
}
