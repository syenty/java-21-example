package com.example.demo.participation.repository;

import com.example.demo.participation.domain.QuizParticipation;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizParticipationRepository extends JpaRepository<QuizParticipation, Long> {
  boolean existsByEvent_IdAndUser_IdAndParticipationDate(Long eventId, Long userId, LocalDate date);

  long countByEvent_IdAndParticipationDateAndIdLessThanEqual(Long eventId, LocalDate date, Long id);
}
