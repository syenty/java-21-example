package com.example.demo.event.dto;

import java.time.LocalDate;

public record EventDailySequenceRequest(Long eventId, LocalDate seqDate, int lastSeq) {}
