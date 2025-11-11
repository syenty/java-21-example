package com.example.demo.participation.repository;

import com.example.demo.participation.domain.EventParticipationAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventParticipationAnswerRepository
    extends JpaRepository<EventParticipationAnswer, Long> {}
