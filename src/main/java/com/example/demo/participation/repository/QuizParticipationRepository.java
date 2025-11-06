package com.example.demo.participation.repository;

import com.example.demo.participation.domain.QuizParticipation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizParticipationRepository extends JpaRepository<QuizParticipation, Long> {
}
