package com.example.demo.participation.controller;

import com.example.demo.common.util.DateUtil;
import com.example.demo.common.dto.PageWrapper;
import com.example.demo.participation.dto.EventParticipationRequest;
import com.example.demo.participation.dto.EventParticipationResponse;
import com.example.demo.participation.dto.EventParticipationSearchParam;
import com.example.demo.participation.service.EventParticipationService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import java.net.URI;
import java.time.Instant;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ModelAttribute;

@RestController
@RequestMapping("/api/event-participations")
@RequiredArgsConstructor
public class EventParticipationController {

  private final EventParticipationService eventParticipationService;

  @GetMapping
  public ResponseEntity<PageWrapper<EventParticipationResponse>> findAll(
      @Valid @ModelAttribute EventParticipationSearchParam params) {

    Instant start = DateUtil.parseUtcDateTime(params.getStartDt());
    Instant end = DateUtil.parseUtcDateTime(params.getEndDt());

    PageWrapper<EventParticipationResponse> page = eventParticipationService.findByEventAndPeriod(
        params.getEventId(),
        start,
        end,
        PageRequest.of(params.getPage(), params.getSize()));

    return ResponseEntity.ok(page);
  }

  @GetMapping("/{id}")
  public ResponseEntity<EventParticipationResponse> findById(@PathVariable Long id) {
    return eventParticipationService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<EventParticipationResponse> create(
      @RequestBody EventParticipationRequest request) {
    return eventParticipationService
        .create(request)
        .map(
            response -> ResponseEntity.created(
                URI.create("/api/event-participations/" + response.id()))
                .body(response))
        .orElse(ResponseEntity.notFound().build());
  }

  @PutMapping("/{id}")
  public ResponseEntity<EventParticipationResponse> update(
      @PathVariable Long id, @RequestBody EventParticipationRequest request) {
    return eventParticipationService
        .update(id, request)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    return eventParticipationService.delete(id)
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }

  @GetMapping("/export")
  public void exportParticipations(
      @RequestParam Long eventId,
      @RequestParam String startDt,
      @RequestParam String endDt,
      HttpServletResponse response) {

    Instant startInstant = DateUtil.parseUtcDateTime(startDt);
    Instant endInstant = DateUtil.parseUtcDateTime(endDt);

    eventParticipationService.downloadExcel(
        eventId,
        startInstant,
        endInstant,
        response);
  }
}
