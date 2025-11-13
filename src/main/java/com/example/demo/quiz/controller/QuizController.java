package com.example.demo.quiz.controller;

import com.example.demo.common.dto.PageWrapper;
import com.example.demo.quiz.dto.QuizAdminResponse;
import com.example.demo.quiz.dto.QuizRequest;
import com.example.demo.quiz.dto.QuizSearchParam;
import com.example.demo.quiz.dto.QuizUserResponse;
import com.example.demo.quiz.service.QuizService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ModelAttribute;

@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
public class QuizController {

  private final QuizService quizService;

  @GetMapping
  public PageWrapper<QuizAdminResponse> findAll(@Valid @ModelAttribute QuizSearchParam params) {
    return quizService.search(params, PageRequest.of(params.getPage(), params.getSize()));
  }

  @GetMapping("/{id}")
  public ResponseEntity<QuizAdminResponse> findById(@PathVariable Long id) {
    return quizService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<QuizAdminResponse> create(@RequestBody QuizRequest request) {
    return quizService
        .create(request)
        .map(
            response ->
                ResponseEntity.created(URI.create("/api/quizzes/" + response.id())).body(response))
        .orElse(ResponseEntity.notFound().build());
  }

  @PutMapping("/{id}")
  public ResponseEntity<QuizAdminResponse> update(
      @PathVariable Long id, @RequestBody QuizRequest request) {
    return quizService.update(id, request).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/events/{eventId}/today")
  public List<QuizUserResponse> findTodayByEvent(@PathVariable Long eventId) {
    return quizService.findTodayByEvent(eventId);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    return quizService.delete(id)
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }
}
