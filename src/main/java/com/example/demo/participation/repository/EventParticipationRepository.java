package com.example.demo.participation.repository;

import com.example.demo.participation.domain.EventParticipation;
import com.example.demo.participation.dto.EventParticipationExcelRow;
import java.time.LocalDate;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventParticipationRepository extends JpaRepository<EventParticipation, Long> {
  boolean existsByEvent_IdAndUser_IdAndParticipationDate(Long eventId, Long userId, LocalDate date);

  long countByEvent_IdAndParticipationDateAndIdLessThanEqual(Long eventId, LocalDate date, Long id);

  @Query("""
      select new com.example.demo.participation.dto.EventParticipationExcelRow(
          ep.participationDt,
          ep.participationDate,
          ep.dailyOrder,
          u.name,
          u.employeeNumber)
      from EventParticipation ep
      join ep.user u
      where ep.event.id = :eventId
        and ep.participationDt between :startDt and :endDt
      order by ep.participationDt asc
      """)
  List<EventParticipationExcelRow> findExcelRows(
      @Param("eventId") Long eventId,
      @Param("startDt") Instant startDt,
      @Param("endDt") Instant endDt);
}
