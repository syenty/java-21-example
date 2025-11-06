package com.example.demo.event.domain;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventDailySequenceId implements Serializable {

  private Long eventId;

  private LocalDate seqDate;

  public EventDailySequenceId(Long eventId, LocalDate seqDate) {
    this.eventId = eventId;
    this.seqDate = seqDate;
  }
}
