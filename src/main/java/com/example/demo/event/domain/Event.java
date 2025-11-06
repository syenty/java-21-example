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
import java.time.LocalDateTime;
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
      final int maxDailyTry,
      final EventStatus status) {
    this.name = name;
    this.description = description;
    this.startDt = startDt;
    this.endDt = endDt;
    this.maxDailyTry = maxDailyTry != 0 ? maxDailyTry : 1;
    this.status = status != null ? status : EventStatus.READY;
  }

  public void updatePeriod(LocalDateTime startDt, LocalDateTime endDt) {
    this.startDt = startDt;
    this.endDt = endDt;
  }

  public void changeStatus(EventStatus status) {
    this.status = status;
  }
}
