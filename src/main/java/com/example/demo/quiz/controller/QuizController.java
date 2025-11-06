package com.example.demo.quiz.controller;

import com.example.demo.quiz.dto.QuizRequest;
import com.example.demo.quiz.dto.QuizResponse;
import com.example.demo.quiz.service.QuizService;
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
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
public class QuizController {

  private final QuizService quizService;

  @GetMapping
  public List<QuizResponse> findAll() {
    return quizService.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<QuizResponse> findById(@PathVariable Long id) {
    return quizService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<QuizResponse> create(@RequestBody QuizRequest request) {
    return quizService
        .create(request)
        .map(
            response ->
                ResponseEntity.created(URI.create("/api/quizzes/" + response.id())).body(response))
        .orElse(ResponseEntity.notFound().build());
  }

  @PutMapping("/{id}")
  public ResponseEntity<QuizResponse> update(
      @PathVariable Long id, @RequestBody QuizRequest request) {
    return quizService.update(id, request).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    return quizService.delete(id)
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }
}
