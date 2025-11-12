package com.example.demo.event.service;

import com.example.demo.event.dto.EventAttendanceResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.List;

public interface EventAttendanceService {
  EventAttendanceResponse attend(Long eventId, Long userId);

  List<EventAttendanceResponse> findByEventAndPeriod(Long eventId, Instant start, Instant end);

  void downloadExcel(Long eventId, Instant start, Instant end, HttpServletResponse response);
}
