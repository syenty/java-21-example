package com.example.demo.event.controller;

import com.example.demo.common.util.DateUtil;
import com.example.demo.event.dto.EventAttendanceResponse;
import com.example.demo.event.service.EventAttendanceService;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/events/{eventId}/attendance")
@RequiredArgsConstructor
public class EventAttendanceController {

  private final EventAttendanceService eventAttendanceService;

  @GetMapping
  public ResponseEntity<List<EventAttendanceResponse>> findByEventAndPeriod(
      @PathVariable Long eventId,
      @RequestParam String startDt,
      @RequestParam String endDt) {

    Instant start = DateUtil.parseUtcDateTime(startDt);
    Instant end = DateUtil.parseUtcDateTime(endDt);

    return ResponseEntity.ok(eventAttendanceService.findByEventAndPeriod(eventId, start, end));
  }

  @GetMapping("/export")
  public void exportAttendances(
      @PathVariable Long eventId,
      @RequestParam String startDt,
      @RequestParam String endDt,
      HttpServletResponse response) {

    Instant start = DateUtil.parseUtcDateTime(startDt);
    Instant end = DateUtil.parseUtcDateTime(endDt);

    eventAttendanceService.downloadExcel(eventId, start, end, response);
  }
}
