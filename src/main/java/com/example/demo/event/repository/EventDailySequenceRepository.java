package com.example.demo.event.repository;

import com.example.demo.event.domain.EventDailySequence;
import com.example.demo.event.domain.EventDailySequenceId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EventDailySequenceRepository
    extends JpaRepository<EventDailySequence, EventDailySequenceId> {
  List<EventDailySequence> findByEvent_Id(Long eventId);
}
