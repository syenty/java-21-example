package com.example.demo.reward.controller;

import com.example.demo.common.dto.PageWrapper;
import com.example.demo.common.util.DateUtil;
import com.example.demo.reward.dto.RewardIssueRequest;
import com.example.demo.reward.dto.RewardIssueResponse;
import com.example.demo.reward.dto.RewardIssueSearchParam;
import com.example.demo.reward.service.RewardIssueService;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URI;
import java.time.Instant;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ModelAttribute;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reward-issues")
@RequiredArgsConstructor
public class RewardIssueController {

  private final RewardIssueService rewardIssueService;

  @GetMapping
  public ResponseEntity<PageWrapper<RewardIssueResponse>> findAll(
      @Valid @ModelAttribute RewardIssueSearchParam params) {

    Instant start = DateUtil.parseUtcDateTime(params.getStartDt());
    Instant end = DateUtil.parseUtcDateTime(params.getEndDt());

    return ResponseEntity.ok(
        rewardIssueService.findByEventAndPeriod(
            params.getEventId(),
            start,
            end,
            PageRequest.of(params.getPage(), params.getSize())));
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

  @GetMapping("/export")
  public void exportRewardIssues(
      @RequestParam(required = false) Long eventId,
      @RequestParam String startDt,
      @RequestParam String endDt,
      HttpServletResponse response) {

    Instant start = DateUtil.parseUtcDateTime(startDt);
    Instant end = DateUtil.parseUtcDateTime(endDt);
    rewardIssueService.downloadExcel(eventId, start, end, response);
  }
}
