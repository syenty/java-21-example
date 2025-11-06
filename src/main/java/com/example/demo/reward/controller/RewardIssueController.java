package com.example.demo.reward.controller;

import com.example.demo.reward.dto.RewardIssueRequest;
import com.example.demo.reward.dto.RewardIssueResponse;
import com.example.demo.reward.service.RewardIssueService;
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
@RequestMapping("/api/reward-issues")
@RequiredArgsConstructor
public class RewardIssueController {

  private final RewardIssueService rewardIssueService;

  @GetMapping
  public List<RewardIssueResponse> findAll() {
    return rewardIssueService.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<RewardIssueResponse> findById(@PathVariable Long id) {
    return rewardIssueService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<RewardIssueResponse> create(@RequestBody RewardIssueRequest request) {
    return rewardIssueService
        .create(request)
        .map(
            response ->
                ResponseEntity.created(URI.create("/api/reward-issues/" + response.id()))
                    .body(response))
        .orElse(ResponseEntity.notFound().build());
  }

  @PutMapping("/{id}")
  public ResponseEntity<RewardIssueResponse> update(
      @PathVariable Long id, @RequestBody RewardIssueRequest request) {
    return rewardIssueService
        .update(id, request)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    return rewardIssueService.delete(id)
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }
}
