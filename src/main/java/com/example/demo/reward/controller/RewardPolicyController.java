package com.example.demo.reward.controller;

import com.example.demo.reward.dto.RewardPolicyRequest;
import com.example.demo.reward.dto.RewardPolicyResponse;
import com.example.demo.reward.service.RewardPolicyService;
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
@RequestMapping("/api/reward-policies")
@RequiredArgsConstructor
public class RewardPolicyController {

  private final RewardPolicyService rewardPolicyService;

  @GetMapping
  public List<RewardPolicyResponse> findAll() {
    return rewardPolicyService.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<RewardPolicyResponse> findById(@PathVariable Long id) {
    return rewardPolicyService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<RewardPolicyResponse> create(@RequestBody RewardPolicyRequest request) {
    return rewardPolicyService
        .create(request)
        .map(
            response ->
                ResponseEntity.created(URI.create("/api/reward-policies/" + response.id()))
                    .body(response))
        .orElse(ResponseEntity.notFound().build());
  }

  @PutMapping("/{id}")
  public ResponseEntity<RewardPolicyResponse> update(
      @PathVariable Long id, @RequestBody RewardPolicyRequest request) {
    return rewardPolicyService
        .update(id, request)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    return rewardPolicyService.delete(id)
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }
}
