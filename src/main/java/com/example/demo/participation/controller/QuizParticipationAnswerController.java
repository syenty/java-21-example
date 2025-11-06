package com.example.demo.participation.controller;

import com.example.demo.participation.dto.QuizParticipationAnswerRequest;
import com.example.demo.participation.dto.QuizParticipationAnswerResponse;
import com.example.demo.participation.service.QuizParticipationAnswerService;
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
@RequestMapping("/api/quiz-participation-answers")
@RequiredArgsConstructor
public class QuizParticipationAnswerController {

  private final QuizParticipationAnswerService answerService;

  @GetMapping
  public List<QuizParticipationAnswerResponse> findAll() {
    return answerService.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<QuizParticipationAnswerResponse> findById(@PathVariable Long id) {
    return answerService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<QuizParticipationAnswerResponse> create(
      @RequestBody QuizParticipationAnswerRequest request) {
    return answerService
        .create(request)
        .map(
            response ->
                ResponseEntity.created(
                        URI.create("/api/quiz-participation-answers/" + response.id()))
                    .body(response))
        .orElse(ResponseEntity.notFound().build());
  }

  @PutMapping("/{id}")
  public ResponseEntity<QuizParticipationAnswerResponse> update(
      @PathVariable Long id, @RequestBody QuizParticipationAnswerRequest request) {
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
