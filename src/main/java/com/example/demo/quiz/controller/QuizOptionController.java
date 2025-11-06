package com.example.demo.quiz.controller;

import com.example.demo.quiz.dto.QuizOptionRequest;
import com.example.demo.quiz.dto.QuizOptionResponse;
import com.example.demo.quiz.service.QuizOptionService;
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
@RequestMapping("/api/quiz-options")
@RequiredArgsConstructor
public class QuizOptionController {

  private final QuizOptionService optionService;

  @GetMapping
  public List<QuizOptionResponse> findAll() {
    return optionService.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<QuizOptionResponse> findById(@PathVariable Long id) {
    return optionService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<QuizOptionResponse> create(@RequestBody QuizOptionRequest request) {
    return optionService
        .create(request)
        .map(
            response ->
                ResponseEntity.created(URI.create("/api/quiz-options/" + response.id()))
                    .body(response))
        .orElse(ResponseEntity.notFound().build());
  }

  @PutMapping("/{id}")
  public ResponseEntity<QuizOptionResponse> update(
      @PathVariable Long id, @RequestBody QuizOptionRequest request) {
    return optionService
        .update(id, request)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    return optionService.delete(id)
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }
}
