package com.example.demo.event.dto;

import com.example.demo.event.domain.Event;
import com.example.demo.event.domain.EventStatus;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record EventResponse(
    Long id,
    String name,
    String description,
    LocalDateTime startDt,
    LocalDateTime endDt,
    LocalTime participationStartTime,
    LocalTime participationEndTime,
    int maxDailyTry,
    EventStatus status,
    Instant createdDt,
    Instant updatedDt) {

  public static EventResponse of(Event event) {
    return new EventResponse(
        event.getId(),
        event.getName(),
        event.getDescription(),
        event.getStartDt(),
        event.getEndDt(),
        event.getParticipationStartTime(),
        event.getParticipationEndTime(),
        event.getMaxDailyTry(),
        event.getStatus(),
        event.getCreatedDt(),
        event.getUpdatedDt());
  }
}
