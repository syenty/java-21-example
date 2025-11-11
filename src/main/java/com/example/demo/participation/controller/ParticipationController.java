package com.example.demo.participation.controller;

import com.example.demo.participation.dto.EventParticipationRequest;
import com.example.demo.participation.dto.ParticipationResult;
import com.example.demo.participation.service.ParticipationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/participations")
@RequiredArgsConstructor
public class ParticipationController {

  private final ParticipationService participationService;

  @PostMapping
  public ResponseEntity<ParticipationResult> participate(
      @RequestBody EventParticipationRequest request) {
    return ResponseEntity.ok(participationService.participate(request));
  }
}
