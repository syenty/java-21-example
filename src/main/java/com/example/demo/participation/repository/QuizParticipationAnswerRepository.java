package com.example.demo.participation.repository;

import com.example.demo.participation.domain.QuizParticipationAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizParticipationAnswerRepository
    extends JpaRepository<QuizParticipationAnswer, Long> {
}
