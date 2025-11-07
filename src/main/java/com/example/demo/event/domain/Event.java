package com.example.demo.event.domain;

import com.example.demo.common.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 200, nullable = false)
  private String name;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(nullable = false)
  private LocalDateTime startDt;

  @Column(nullable = false)
  private LocalDateTime endDt;

  @Column(nullable = false)
  private LocalTime participationStartTime;

  @Column(nullable = false)
  private LocalTime participationEndTime;

  @Column(nullable = false)
  private int maxDailyTry = 1;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private EventStatus status = EventStatus.READY;

  @Builder
  private Event(
      final String name,
      final String description,
      final LocalDateTime startDt,
      final LocalDateTime endDt,
      final LocalTime participationStartTime,
      final LocalTime participationEndTime,
      final int maxDailyTry,
      final EventStatus status) {
    this.name = name;
    this.description = description;
    this.startDt = startDt;
    this.endDt = endDt;
    this.participationStartTime =
        Objects.requireNonNull(participationStartTime, "participationStartTime must not be null");
    this.participationEndTime =
        Objects.requireNonNull(participationEndTime, "participationEndTime must not be null");
    this.maxDailyTry = maxDailyTry != 0 ? maxDailyTry : 1;
    this.status = status != null ? status : EventStatus.READY;
  }

  public void updatePeriod(LocalDateTime startDt, LocalDateTime endDt) {
    this.startDt = startDt;
    this.endDt = endDt;
  }

  public void updateDetail(
      String name,
      String description,
      LocalDateTime startDt,
      LocalDateTime endDt,
      LocalTime participationStartTime,
      LocalTime participationEndTime,
      Integer maxDailyTry,
      EventStatus status) {
    this.name = name;
    this.description = description;
    this.startDt = startDt;
    this.endDt = endDt;
    if (participationStartTime != null) {
      this.participationStartTime = participationStartTime;
    }
    if (participationEndTime != null) {
      this.participationEndTime = participationEndTime;
    }
    if (maxDailyTry != null && maxDailyTry > 0) {
      this.maxDailyTry = maxDailyTry;
    }
    if (status != null) {
      this.status = status;
    }
  }

  public void changeStatus(EventStatus status) {
    this.status = status;
  }

  public boolean isWithinParticipationWindow(Instant now, ZoneId zoneId) {
    LocalDateTime current = LocalDateTime.ofInstant(now, zoneId);
    if (current.isBefore(startDt) || current.isAfter(endDt)) {
      return false;
    }
    LocalTime currentTime = current.toLocalTime();
    return !currentTime.isBefore(participationStartTime)
        && !currentTime.isAfter(participationEndTime);
  }
}
