package com.example.demo.event.repository;

import com.example.demo.event.domain.EventAttendance;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventAttendanceRepository extends JpaRepository<EventAttendance, Long> {
  boolean existsByEvent_IdAndUser_IdAndAttendanceDate(Long eventId, Long userId, LocalDate date);

  Optional<EventAttendance> findByEvent_IdAndUser_IdAndAttendanceDate(
      Long eventId, Long userId, LocalDate date);
}
