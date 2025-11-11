package com.example.demo.participation.service.impl;

import com.example.demo.event.domain.Event;
import com.example.demo.event.repository.EventRepository;
import com.example.demo.participation.domain.EventParticipation;
import com.example.demo.participation.dto.EventParticipationRequest;
import com.example.demo.participation.dto.EventParticipationResponse;
import com.example.demo.participation.repository.EventParticipationRepository;
import com.example.demo.participation.service.EventParticipationService;
import com.example.demo.quiz.service.QuizService;
import com.example.demo.user.domain.User;
import com.example.demo.user.repository.UserRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventParticipationServiceImpl implements EventParticipationService {

  private static final ZoneId ZONE_ID = ZoneId.of("Asia/Seoul");

  private final EventParticipationRepository eventParticipationRepository;
  private final EventRepository eventRepository;
  private final UserRepository userRepository;
  private final QuizService quizService;

  @Override
  public List<EventParticipationResponse> findAll() {
    return eventParticipationRepository.findAll().stream()
        .map(EventParticipationResponse::of)
        .toList();
  }

  @Override
  public Optional<EventParticipationResponse> findById(Long id) {
    return eventParticipationRepository.findById(id).map(EventParticipationResponse::of);
  }

  @Override
  @Transactional
  public Optional<EventParticipationResponse> create(EventParticipationRequest request) {
    if (!quizService.areAllAnswersCorrect(request.eventId(), request.answers())) {
      throw new IllegalStateException("모든 퀴즈 정답을 맞춰야 참여가 인정됩니다.");
    }
    return eventRepository
        .findById(request.eventId())
        .flatMap(
            event -> userRepository
                .findById(request.userId())
                .map(
                    user -> {
                      Instant participationInstant = Instant.now();
                      LocalDate participationDate = LocalDate.now(ZONE_ID);

                      if (!event.isWithinParticipationWindow(participationInstant, ZONE_ID)) {
                        throw new IllegalStateException("지정된 참여 시간이 아닙니다.");
                      }

                      if (eventParticipationRepository.existsByEvent_IdAndUser_IdAndParticipationDate(
                          event.getId(), user.getId(), participationDate)) {
                        throw new IllegalStateException("이미 퀴즈를 참여했습니다.");
                      }

                      EventParticipation participation = EventParticipation.builder()
                          .event(event)
                          .user(user)
                          .participationDt(participationInstant)
                          .participationDate(participationDate)
                          .dailyOrder(1)
                          .correct(true)
                          .correctCount(request.answers() != null ? request.answers().size() : 0)
                          .totalQuestions(request.answers() != null ? request.answers().size() : 0)
                          .build();
                      return EventParticipationResponse.of(
                          eventParticipationRepository.save(participation));
                    }));
  }

  @Override
  @Transactional
  public Optional<EventParticipationResponse> update(Long id, EventParticipationRequest request) {
    return eventParticipationRepository
        .findById(id)
        .flatMap(
            participation -> {
              if (request.eventId() != null
                  && !participation.getEvent().getId().equals(request.eventId())) {
                Optional<Event> eventOpt = eventRepository.findById(request.eventId());
                if (eventOpt.isEmpty()) {
                  return Optional.<EventParticipationResponse>empty();
                }
                participation.changeEvent(eventOpt.get());
              }
              if (request.userId() != null
                  && !participation.getUser().getId().equals(request.userId())) {
                Optional<User> userOpt = userRepository.findById(request.userId());
                if (userOpt.isEmpty()) {
                  return Optional.<EventParticipationResponse>empty();
                }
                participation.changeUser(userOpt.get());
              }
              // no mutable fields from request anymore
              return Optional.of(EventParticipationResponse.of(participation));
            });
  }

  @Override
  @Transactional
  public boolean delete(Long id) {
    if (!eventParticipationRepository.existsById(id)) {
      return false;
    }
    eventParticipationRepository.deleteById(id);
    return true;
  }
}
