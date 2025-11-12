package com.example.demo.event.service.impl;

import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.common.util.ExcelUtil;
import com.example.demo.common.util.StringUtil;
import com.example.demo.event.domain.Event;
import com.example.demo.event.domain.EventAttendance;
import com.example.demo.event.dto.EventAttendanceExcelRow;
import com.example.demo.event.dto.EventAttendanceResponse;
import com.example.demo.event.repository.EventAttendanceRepository;
import com.example.demo.event.repository.EventRepository;
import com.example.demo.event.service.EventAttendanceService;
import com.example.demo.user.domain.User;
import com.example.demo.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventAttendanceServiceImpl implements EventAttendanceService {

  private static final ZoneId ZONE_ID = ZoneId.of("Asia/Seoul");

  private final EventAttendanceRepository attendanceRepository;
  private final EventRepository eventRepository;
  private final UserRepository userRepository;

  private static final List<String> ATTENDANCE_HEADERS = List.of(
      "attendanceDt",
      "attendanceDate",
      "name",
      "employeeNumber");

  @Override
  @Transactional
  public EventAttendanceResponse attend(Long eventId, Long userId) {
    Event event = eventRepository
        .findById(eventId)
        .orElseThrow(() -> new BusinessException(ErrorCode.EVENT_NOT_FOUND));
    User user = userRepository
        .findById(userId)
        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

    Instant now = Instant.now();
    LocalDate today = LocalDate.now(ZONE_ID);

    Optional<EventAttendance> existingAttendance = attendanceRepository
        .findByEvent_IdAndUser_IdAndAttendanceDate(eventId, userId, today);
    if (existingAttendance.isPresent()) {
      return EventAttendanceResponse.of(existingAttendance.get());
    }

    if (!event.isWithinParticipationWindow(now, ZONE_ID)) {
      throw new BusinessException(ErrorCode.EVENT_PARTICIPATION_WINDOW_CLOSED);
    }

    EventAttendance attendance = EventAttendance.builder()
        .event(event)
        .user(user)
        .attendanceDt(now)
        .attendanceDate(today)
        .build();

    return EventAttendanceResponse.of(attendanceRepository.save(attendance));
  }

  @Override
  public List<EventAttendanceResponse> findByEventAndPeriod(Long eventId, Instant start, Instant end) {
    validatePeriod(start, end);
    return attendanceRepository.findAttendances(eventId, start, end)
        .stream()
        .map(EventAttendanceResponse::of)
        .toList();
  }

  @Override
  public void downloadExcel(Long eventId, Instant start, Instant end, HttpServletResponse response) {
    validatePeriod(start, end);
    List<EventAttendanceExcelRow> rows = attendanceRepository.findExcelRows(eventId, start, end);

    List<List<String>> body = rows.stream()
        .map(row -> List.of(
            StringUtil.formatInstant(row.attendanceDt()),
            StringUtil.formatLocalDate(row.attendanceDate()),
            StringUtil.defaultString(row.userName()),
            StringUtil.defaultString(row.employeeNumber())))
        .toList();

    ExcelUtil.downloadXlsx(
        response,
        "event-attendance.xlsx",
        ATTENDANCE_HEADERS,
        body);
  }

  private void validatePeriod(Instant start, Instant end) {
    if (start == null || end == null || start.isAfter(end)) {
      throw new IllegalArgumentException("유효한 기간이 필요합니다.");
    }
  }
}
