package com.example.demo.event.service.impl;

import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.event.domain.Event;
import com.example.demo.event.domain.EventAttendance;
import com.example.demo.event.dto.EventAttendanceResponse;
import com.example.demo.event.repository.EventAttendanceRepository;
import com.example.demo.event.repository.EventRepository;
import com.example.demo.event.service.EventAttendanceService;
import com.example.demo.user.domain.User;
import com.example.demo.user.repository.UserRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
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
      throw new IllegalStateException("지정된 참여 시간이 아닙니다.");
    }

    EventAttendance attendance = EventAttendance.builder()
        .event(event)
        .user(user)
        .attendanceDt(now)
        .attendanceDate(today)
        .build();

    return EventAttendanceResponse.of(attendanceRepository.save(attendance));
  }
}
