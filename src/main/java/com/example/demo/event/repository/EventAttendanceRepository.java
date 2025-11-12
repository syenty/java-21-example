package com.example.demo.event.repository;

import com.example.demo.event.domain.EventAttendance;
import com.example.demo.event.dto.EventAttendanceExcelRow;
import java.time.LocalDate;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventAttendanceRepository extends JpaRepository<EventAttendance, Long> {
  boolean existsByEvent_IdAndUser_IdAndAttendanceDate(Long eventId, Long userId, LocalDate date);

  Optional<EventAttendance> findByEvent_IdAndUser_IdAndAttendanceDate(
      Long eventId, Long userId, LocalDate date);

  @Query("""
      select ea from EventAttendance ea
      join fetch ea.user
      where ea.event.id = :eventId
        and ea.attendanceDt between :startDt and :endDt
      order by ea.attendanceDt asc
      """)
  List<EventAttendance> findAttendances(
      @Param("eventId") Long eventId,
      @Param("startDt") Instant startDt,
      @Param("endDt") Instant endDt);

  @Query("""
      select new com.example.demo.event.dto.EventAttendanceExcelRow(
          ea.attendanceDt,
          ea.attendanceDate,
          u.name,
          u.employeeNumber)
      from EventAttendance ea
      join ea.user u
      where ea.event.id = :eventId
        and ea.attendanceDt between :startDt and :endDt
      order by ea.attendanceDt asc
      """)
  List<EventAttendanceExcelRow> findExcelRows(
      @Param("eventId") Long eventId,
      @Param("startDt") Instant startDt,
      @Param("endDt") Instant endDt);
}
