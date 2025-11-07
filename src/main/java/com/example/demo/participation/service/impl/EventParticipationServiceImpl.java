package com.example.demo.participation.service.impl;

import com.example.demo.event.domain.Event;
import com.example.demo.event.domain.EventDailySequence;
import com.example.demo.event.domain.EventDailySequenceId;
import com.example.demo.event.domain.EventStatus;
import com.example.demo.event.repository.EventDailySequenceRepository;
import com.example.demo.event.repository.EventRepository;
import com.example.demo.participation.domain.QuizParticipation;
import com.example.demo.participation.dto.EventParticipationResult;
import com.example.demo.participation.dto.QuizParticipationRequest;
import com.example.demo.participation.dto.QuizParticipationResponse;
import com.example.demo.participation.repository.QuizParticipationRepository;
import com.example.demo.participation.service.EventParticipationService;
import com.example.demo.quiz.service.QuizService;
import com.example.demo.reward.domain.RewardPolicy;
import com.example.demo.reward.dto.RewardIssueResponse;
import com.example.demo.reward.service.RewardIssueService;
import com.example.demo.reward.service.RewardPolicyService;
import com.example.demo.user.domain.User;
import com.example.demo.user.repository.UserRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventParticipationServiceImpl implements EventParticipationService {

  private static final ZoneId ZONE_ID = ZoneId.of("Asia/Seoul");

  private final EventRepository eventRepository;
  private final UserRepository userRepository;
  private final QuizParticipationRepository participationRepository;
  private final EventDailySequenceRepository dailySequenceRepository;
  private final RewardPolicyService rewardPolicyService;
  private final RewardIssueService rewardIssueService;
  private final QuizService quizService;

  @Override
  @Transactional
  public EventParticipationResult participate(QuizParticipationRequest request) {

    ZonedDateTime nowUtc = ZonedDateTime.now(ZoneOffset.UTC);
    Instant participationInstant = nowUtc.toInstant();
    LocalDate participationDate = nowUtc.withZoneSameInstant(ZONE_ID).toLocalDate();

    Event event = eventRepository
        .findById(request.eventId())
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이벤트입니다."));

    User user = userRepository
        .findById(request.userId())
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

    validateEvent(event, participationInstant);

    ensureNotParticipatedToday(event.getId(), user.getId(), participationDate);
    ensureAnsweredCorrectly(event.getId(), request);

    int dailyOrder = nextDailyOrder(event, participationDate);

    QuizParticipation participation = participationRepository.save(
        QuizParticipation.builder()
            .event(event)
            .user(user)
            .participationDt(participationInstant)
            .participationDate(participationDate)
            .dailyOrder(dailyOrder)
            .correct(true)
            .score(0)
            .correctCount(request.answers() != null ? request.answers().size() : 0)
            .totalQuestions(request.answers() != null ? request.answers().size() : 0)
            .build());

    List<RewardPolicy> policies = rewardPolicyService.findActivePolicies(event.getId(), participationInstant);
    RewardIssueResponse reward = rewardIssueService.decideAndIssue(policies, participation, participationDate)
        .orElse(null);

    return new EventParticipationResult(QuizParticipationResponse.of(participation), reward);
  }

  private void validateEvent(Event event, Instant utcNow) {
    if (event.getStatus() != EventStatus.OPEN) {
      throw new IllegalStateException("진행 중인 이벤트가 아닙니다.");
    }
    if (utcNow.isBefore(event.getStartDt().toInstant(ZoneOffset.UTC))
        || utcNow.isAfter(event.getEndDt().toInstant(ZoneOffset.UTC))) {
      throw new IllegalStateException("이벤트 기간이 아닙니다.");
    }
  }

  private void ensureNotParticipatedToday(Long eventId, Long userId, LocalDate date) {
    if (participationRepository.existsByEvent_IdAndUser_IdAndParticipationDate(
        eventId, userId, date)) {
      throw new IllegalStateException("이미 오늘 참여했습니다.");
    }
  }

  private void ensureAnsweredCorrectly(Long eventId, QuizParticipationRequest request) {
    if (!quizService.areAllAnswersCorrect(eventId, request.answers())) {
      throw new IllegalStateException("퀴즈 정답을 맞춰야 참여가 인정됩니다.");
    }
  }

  private int nextDailyOrder(Event event, LocalDate date) {
    EventDailySequenceId id = new EventDailySequenceId(event.getId(), date);
    EventDailySequence sequence = dailySequenceRepository
        .findById(id)
        .orElseGet(
            () -> EventDailySequence.builder()
                .event(event)
                .id(id)
                .lastSequence(0)
                .build());

    sequence.updateLastSequence(sequence.getLastSequence() + 1);
    dailySequenceRepository.save(sequence);
    return sequence.getLastSequence();
  }

}
