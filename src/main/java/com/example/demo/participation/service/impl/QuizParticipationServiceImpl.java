package com.example.demo.participation.service.impl;

import com.example.demo.event.domain.Event;
import com.example.demo.event.repository.EventRepository;
import com.example.demo.participation.domain.QuizParticipation;
import com.example.demo.participation.dto.QuizParticipationRequest;
import com.example.demo.participation.dto.QuizParticipationResponse;
import com.example.demo.participation.repository.QuizParticipationRepository;
import com.example.demo.participation.service.QuizParticipationService;
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
public class QuizParticipationServiceImpl implements QuizParticipationService {

  private static final ZoneId ZONE_ID = ZoneId.of("Asia/Seoul");

  private final QuizParticipationRepository participationRepository;
  private final EventRepository eventRepository;
  private final UserRepository userRepository;

  @Override
  public List<QuizParticipationResponse> findAll() {
    return participationRepository.findAll().stream()
        .map(QuizParticipationResponse::of)
        .toList();
  }

  @Override
  public Optional<QuizParticipationResponse> findById(Long id) {
    return participationRepository.findById(id).map(QuizParticipationResponse::of);
  }

  @Override
  @Transactional
  public Optional<QuizParticipationResponse> create(QuizParticipationRequest request) {
    if (request.correct() == null || !request.correct()) {
      throw new IllegalStateException("정답을 맞춘 경우에만 참여가 인정됩니다.");
    }
    return eventRepository
        .findById(request.eventId())
        .flatMap(
            event -> userRepository
                .findById(request.userId())
                .map(
                    user -> {
                      Instant participationInstant = request.participationDt() != null
                          ? request.participationDt()
                          : Instant.now();
                      LocalDate participationDate = request.participationDate() != null
                          ? request.participationDate()
                          : LocalDate.now(ZONE_ID);

                      if (!event.isWithinParticipationWindow(participationInstant, ZONE_ID)) {
                        throw new IllegalStateException("지정된 참여 시간이 아닙니다.");
                      }

                      if (participationRepository.existsByEvent_IdAndUser_IdAndParticipationDate(
                          event.getId(), user.getId(), participationDate)) {
                        throw new IllegalStateException("이미 퀴즈를 참여했습니다.");
                      }

                      QuizParticipation participation = QuizParticipation.builder()
                          .event(event)
                          .user(user)
                          .participationDt(participationInstant)
                          .participationDate(participationDate)
                          .dailyOrder(
                              request.dailyOrder() != null ? request.dailyOrder() : 1)
                          .correct(request.correct() != null && request.correct())
                          .score(request.score() != null ? request.score() : 0)
                          .correctCount(
                              request.correctCount() != null ? request.correctCount() : 0)
                          .totalQuestions(
                              request.totalQuestions() != null
                                  ? request.totalQuestions()
                                  : 0)
                          .build();
                      return QuizParticipationResponse.of(
                          participationRepository.save(participation));
                    }));
  }

  @Override
  @Transactional
  public Optional<QuizParticipationResponse> update(Long id, QuizParticipationRequest request) {
    return participationRepository
        .findById(id)
        .flatMap(
            participation -> {
              if (request.eventId() != null
                  && !participation.getEvent().getId().equals(request.eventId())) {
                Optional<Event> eventOpt = eventRepository.findById(request.eventId());
                if (eventOpt.isEmpty()) {
                  return Optional.<QuizParticipationResponse>empty();
                }
                participation.changeEvent(eventOpt.get());
              }
              if (request.userId() != null
                  && !participation.getUser().getId().equals(request.userId())) {
                Optional<User> userOpt = userRepository.findById(request.userId());
                if (userOpt.isEmpty()) {
                  return Optional.<QuizParticipationResponse>empty();
                }
                participation.changeUser(userOpt.get());
              }
              participation.update(
                  request.participationDt(),
                  request.participationDate(),
                  request.dailyOrder(),
                  request.correct(),
                  request.score(),
                  request.correctCount(),
                  request.totalQuestions());
              return Optional.of(QuizParticipationResponse.of(participation));
            });
  }

  @Override
  @Transactional
  public boolean delete(Long id) {
    if (!participationRepository.existsById(id)) {
      return false;
    }
    participationRepository.deleteById(id);
    return true;
  }
}
