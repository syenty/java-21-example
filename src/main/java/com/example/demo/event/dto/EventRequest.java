package com.example.demo.event.dto;

import com.example.demo.event.domain.EventStatus;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record EventRequest(
    String name,
    String description,
    LocalDateTime startDt,
    LocalDateTime endDt,
    LocalTime participationStartTime,
    LocalTime participationEndTime,
    Integer maxDailyTry,
    EventStatus status,
    Integer rewardLimitPerUser) {
}
