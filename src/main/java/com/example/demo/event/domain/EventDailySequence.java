package com.example.demo.event.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventDailySequence {

  @EmbeddedId
  private EventDailySequenceId id;

  @MapsId("eventId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Event event;

  @Column(name = "last_seq", nullable = false)
  private int lastSeq;

  @Builder
  private EventDailySequence(Event event, EventDailySequenceId id, int lastSeq) {
    this.event = event;
    this.id = id;
    this.lastSeq = lastSeq;
  }

  public void updateLastSeq(int lastSeq) {
    this.lastSeq = lastSeq;
  }
}
