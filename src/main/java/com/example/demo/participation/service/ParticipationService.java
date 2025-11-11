package com.example.demo.participation.service;

import com.example.demo.participation.dto.EventParticipationRequest;
import com.example.demo.participation.dto.ParticipationResult;

public interface ParticipationService {
  ParticipationResult participate(EventParticipationRequest request);
}
