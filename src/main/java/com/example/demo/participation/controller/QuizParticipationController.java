package com.example.demo.participation.controller;

import com.example.demo.participation.dto.QuizParticipationRequest;
import com.example.demo.participation.dto.QuizParticipationResponse;
import com.example.demo.participation.service.QuizParticipationService;
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
@RequestMapping("/api/quiz-participations")
@RequiredArgsConstructor
public class QuizParticipationController {

  private final QuizParticipationService participationService;

  @GetMapping
  public List<QuizParticipationResponse> findAll() {
    return participationService.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<QuizParticipationResponse> findById(@PathVariable Long id) {
    return participationService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<QuizParticipationResponse> create(
      @RequestBody QuizParticipationRequest request) {
    return participationService
        .create(request)
        .map(
            response ->
                ResponseEntity.created(
                        URI.create("/api/quiz-participations/" + response.id()))
                    .body(response))
        .orElse(ResponseEntity.notFound().build());
  }

  @PutMapping("/{id}")
  public ResponseEntity<QuizParticipationResponse> update(
      @PathVariable Long id, @RequestBody QuizParticipationRequest request) {
    return participationService
        .update(id, request)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    return participationService.delete(id)
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }
}
