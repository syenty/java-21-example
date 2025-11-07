package com.example.demo.event.domain;

import com.example.demo.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
    name = "event_attendance",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "uq_event_attendance",
          columnNames = {"event_id", "user_id", "attendance_date"})
    })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventAttendance {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "event_id")
  private Event event;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "attendance_dt", nullable = false)
  private Instant attendanceDt;

  @Column(name = "attendance_date", nullable = false)
  private LocalDate attendanceDate;

  @Builder
  private EventAttendance(
      Event event, User user, Instant attendanceDt, LocalDate attendanceDate) {
    this.event = event;
    this.user = user;
    this.attendanceDt = attendanceDt;
    this.attendanceDate = attendanceDate;
  }
}
