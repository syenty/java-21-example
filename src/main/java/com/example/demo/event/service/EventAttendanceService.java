package com.example.demo.event.service;

import com.example.demo.event.dto.EventAttendanceResponse;

public interface EventAttendanceService {
  EventAttendanceResponse attend(Long eventId, Long userId);
}
