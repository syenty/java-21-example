package com.example.demo.participation.controller;

import com.example.demo.participation.dto.EventParticipationResult;
import com.example.demo.participation.dto.QuizParticipationRequest;
import com.example.demo.participation.service.EventParticipationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/event-participations")
@RequiredArgsConstructor
public class EventParticipationController {

  private final EventParticipationService eventParticipationService;

  @PostMapping
  public ResponseEntity<EventParticipationResult> participate(
      @RequestBody QuizParticipationRequest request) {
    return ResponseEntity.ok(eventParticipationService.participate(request));
  }
}
