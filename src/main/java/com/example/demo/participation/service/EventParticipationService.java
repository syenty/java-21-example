package com.example.demo.participation.service;

import com.example.demo.participation.dto.EventParticipationResult;
import com.example.demo.participation.dto.QuizParticipationRequest;

public interface EventParticipationService {
  EventParticipationResult participate(QuizParticipationRequest request);
}
