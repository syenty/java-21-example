package com.example.demo.event.service;

import com.example.demo.common.dto.PageWrapper;
import com.example.demo.event.dto.EventAttendanceResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface EventAttendanceService {
  EventAttendanceResponse attend(Long eventId, Long userId);

  PageWrapper<EventAttendanceResponse> findByEventAndPeriod(Long eventId, Instant start, Instant end, Pageable pageable);

  void downloadExcel(Long eventId, Instant start, Instant end, HttpServletResponse response);
}
