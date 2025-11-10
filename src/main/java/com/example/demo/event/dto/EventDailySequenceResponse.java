package com.example.demo.event.dto;

import com.example.demo.event.domain.EventDailySequence;
import java.time.LocalDate;

public record EventDailySequenceResponse(Long eventId, LocalDate seqDate, int lastSeq) {

  public static EventDailySequenceResponse of(EventDailySequence sequence) {
    return new EventDailySequenceResponse(
        sequence.getEvent().getId(), sequence.getId().getSeqDate(), sequence.getLastSeq());
  }
}
