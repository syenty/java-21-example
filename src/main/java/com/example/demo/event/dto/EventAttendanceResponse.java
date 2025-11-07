package com.example.demo.event.dto;

import com.example.demo.event.domain.EventAttendance;
import java.time.Instant;
import java.time.LocalDate;

public record EventAttendanceResponse(
    Long id, Long eventId, Long userId, Instant attendanceDt, LocalDate attendanceDate) {

  public static EventAttendanceResponse of(EventAttendance attendance) {
    return new EventAttendanceResponse(
        attendance.getId(),
        attendance.getEvent().getId(),
        attendance.getUser().getId(),
        attendance.getAttendanceDt(),
        attendance.getAttendanceDate());
  }
}
