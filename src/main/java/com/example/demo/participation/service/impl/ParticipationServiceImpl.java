package com.example.demo.participation.service.impl;

import com.example.demo.common.util.DateUtil;
import com.example.demo.event.domain.Event;
import com.example.demo.event.domain.EventDailySequence;
import com.example.demo.event.domain.EventDailySequenceId;
import com.example.demo.event.repository.EventDailySequenceRepository;
import com.example.demo.event.repository.EventRepository;
import com.example.demo.participation.domain.EventParticipation;
import com.example.demo.participation.domain.EventParticipationAnswer;
import com.example.demo.participation.dto.EventParticipationRequest;
import com.example.demo.participation.dto.EventParticipationResponse;
import com.example.demo.participation.dto.ParticipationResult;
import com.example.demo.participation.dto.EventParticipationRequest.QuizAnswer;
import com.example.demo.participation.repository.EventParticipationRepository;
import com.example.demo.participation.repository.EventParticipationAnswerRepository;
import com.example.demo.participation.service.ParticipationService;
import com.example.demo.quiz.service.QuizService;
import com.example.demo.reward.domain.RewardPolicy;
import com.example.demo.reward.dto.RewardIssueResponse;
import com.example.demo.reward.service.RewardIssueService;
import com.example.demo.reward.service.RewardPolicyService;
import com.example.demo.quiz.domain.Quiz;
import com.example.demo.quiz.domain.QuizOption;
import com.example.demo.quiz.repository.QuizRepository;
import com.example.demo.quiz.repository.QuizOptionRepository;
import com.example.demo.user.domain.User;
import com.example.demo.user.repository.UserRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ParticipationServiceImpl implements ParticipationService {

  private final EventRepository eventRepository;
  private final UserRepository userRepository;
  private final EventParticipationRepository eventParticipationRepository;
  private final EventDailySequenceRepository eventDailySequenceRepository;
  private final EventParticipationAnswerRepository eventParticipationAnswerRepository;
  private final RewardPolicyService rewardPolicyService;
  private final RewardIssueService rewardIssueService;
  private final QuizService quizService;
  private final QuizRepository quizRepository;
  private final QuizOptionRepository quizOptionRepository;

  @Override
  @Transactional
  public ParticipationResult participate(EventParticipationRequest request) {

    ZonedDateTime nowUtc = DateUtil.nowUtc();
    Instant participationInstant = nowUtc.toInstant();
    LocalDate participationDate = DateUtil.utcToAsiaSeoulDate(nowUtc);

    Event event = eventRepository
        .findById(request.eventId())
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이벤트입니다."));

    User user = userRepository
        .findById(request.userId())
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

    event.ensureActiveDuring(participationInstant);

    ensureNotParticipatedToday(event.getId(), user.getId(), participationDate);
    ensureAnsweredCorrectly(event.getId(), request);

    int dailyOrder = nextDailyOrder(event, participationDate);

    EventParticipation participation = eventParticipationRepository.save(
        EventParticipation.builder()
            .event(event)
            .user(user)
            .participationDt(participationInstant)
            .participationDate(participationDate)
            .dailyOrder(dailyOrder)
            .correct(true)
            .correctCount(request.answers() != null ? request.answers().size() : 0)
            .totalQuestions(request.answers() != null ? request.answers().size() : 0)
            .build());

    saveParticipationAnswers(participation, request.answers(), participationInstant);

    List<RewardPolicy> policies = rewardPolicyService.findActivePolicies(event.getId(), participationInstant);
    RewardIssueResponse reward = rewardIssueService.decideAndIssue(policies, participation, participationDate)
        .orElse(null);

    return new ParticipationResult(EventParticipationResponse.of(participation), reward);
  }

  private void ensureNotParticipatedToday(Long eventId, Long userId, LocalDate date) {
    if (eventParticipationRepository.existsByEvent_IdAndUser_IdAndParticipationDate(
        eventId, userId, date)) {
      throw new IllegalStateException("이미 오늘 참여했습니다.");
    }
  }

  private void ensureAnsweredCorrectly(Long eventId, EventParticipationRequest request) {
    if (!quizService.areAllAnswersCorrect(eventId, request.answers())) {
      throw new IllegalStateException("퀴즈 정답을 맞춰야 참여가 인정됩니다.");
    }
  }

  private int nextDailyOrder(Event event, LocalDate date) {
    EventDailySequenceId id = new EventDailySequenceId(event.getId(), date);
    EventDailySequence sequence = eventDailySequenceRepository
        .findById(id)
        .orElseGet(
            () -> EventDailySequence.builder()
                .event(event)
                .id(id)
                .lastSeq(0)
                .build());

    sequence.updateLastSeq(sequence.getLastSeq() + 1);
    eventDailySequenceRepository.save(sequence);
    return sequence.getLastSeq();
  }

  private void saveParticipationAnswers(
      EventParticipation participation,
      List<QuizAnswer> answers,
      Instant answeredAt) {
    if (answers == null || answers.isEmpty()) {
      return;
    }

    List<EventParticipationAnswer> entities = answers.stream()
        .map(answer -> buildParticipationAnswer(participation, answer, answeredAt))
        .toList();

    eventParticipationAnswerRepository.saveAll(entities);
  }

  private EventParticipationAnswer buildParticipationAnswer(
      EventParticipation participation,
      QuizAnswer answer,
      Instant answeredAt) {

    if (answer == null || answer.quizId() == null) {
      throw new IllegalArgumentException("퀴즈 정보가 올바르지 않습니다.");
    }

    Quiz quiz = quizRepository.findById(answer.quizId())
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 퀴즈입니다."));

    if (!quiz.getEvent().getId().equals(participation.getEvent().getId())) {
      throw new IllegalArgumentException("해당 이벤트의 퀴즈만 응답할 수 있습니다.");
    }

    QuizOption option = null;
    if (answer.optionId() != null) {
      option = quizOptionRepository.findById(answer.optionId())
          .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 선택지입니다."));
      if (!option.getQuiz().getId().equals(quiz.getId())) {
        throw new IllegalArgumentException("퀴즈와 선택지가 일치하지 않습니다.");
      }
    }

    return EventParticipationAnswer.builder()
        .participation(participation)
        .quiz(quiz)
        .option(option)
        .answerText(answer.answerText())
        .correct(true)
        .answerDt(answeredAt)
        .build();
  }
}
