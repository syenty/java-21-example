package com.example.demo.participation.controller;

import com.example.demo.participation.dto.EventParticipationAnswerRequest;
import com.example.demo.participation.dto.EventParticipationAnswerResponse;
import com.example.demo.participation.service.EventParticipationAnswerService;
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
@RequestMapping("/api/event-participation-answers")
@RequiredArgsConstructor
public class EventParticipationAnswerController {

  private final EventParticipationAnswerService answerService;

  @GetMapping
  public List<EventParticipationAnswerResponse> findAll() {
    return answerService.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<EventParticipationAnswerResponse> findById(@PathVariable Long id) {
    return answerService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<EventParticipationAnswerResponse> create(
      @RequestBody EventParticipationAnswerRequest request) {
    return answerService
        .create(request)
        .map(
            response ->
                ResponseEntity.created(
                        URI.create("/api/event-participation-answers/" + response.id()))
                    .body(response))
        .orElse(ResponseEntity.notFound().build());
  }

  @PutMapping("/{id}")
  public ResponseEntity<EventParticipationAnswerResponse> update(
      @PathVariable Long id, @RequestBody EventParticipationAnswerRequest request) {
    return answerService
        .update(id, request)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    return answerService.delete(id)
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }
}
