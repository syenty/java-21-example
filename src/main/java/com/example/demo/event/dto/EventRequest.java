package com.example.demo.event.dto;

import com.example.demo.event.domain.EventStatus;
import java.time.LocalDateTime;

public record EventRequest(
    String name,
    String description,
    LocalDateTime startDt,
    LocalDateTime endDt,
    Integer maxDailyTry,
    EventStatus status) {}
