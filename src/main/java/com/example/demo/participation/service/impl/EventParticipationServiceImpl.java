package com.example.demo.participation.service.impl;

import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.common.util.DateUtil;
import com.example.demo.common.util.ExcelUtil;
import com.example.demo.common.util.StringUtil;
import com.example.demo.event.domain.Event;
import com.example.demo.event.repository.EventRepository;
import com.example.demo.participation.domain.EventParticipation;
import com.example.demo.participation.dto.EventParticipationRequest;
import com.example.demo.participation.dto.EventParticipationResponse;
import com.example.demo.participation.dto.EventParticipationExcelRow;
import com.example.demo.participation.repository.EventParticipationRepository;
import com.example.demo.participation.service.EventParticipationService;
import com.example.demo.quiz.service.QuizService;
import com.example.demo.user.domain.User;
import com.example.demo.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
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
  private static final List<String> PARTICIPATION_HEADERS = List.of(
      "participationDt",
      "participationDate",
      "dailyOrder",
      "name",
      "employeeNumber");

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
      throw new BusinessException(ErrorCode.QUIZ_ANSWER_INCORRECT);
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
                        throw new BusinessException(ErrorCode.EVENT_PARTICIPATION_WINDOW_CLOSED);
                      }

                      if (eventParticipationRepository.existsByEvent_IdAndUser_IdAndParticipationDate(
                          event.getId(), user.getId(), participationDate)) {
                        throw new BusinessException(ErrorCode.PARTICIPATION_ALREADY_DONE);
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

  @Override
  public List<EventParticipationResponse> findByEventAndPeriod(Long eventId, Instant start, Instant end) {
    validatePeriod(start, end);
    return eventParticipationRepository.findParticipations(eventId, start, end).stream()
        .map(EventParticipationResponse::of)
        .toList();
  }

  @Override
  public void downloadExcel(Long eventId, Instant start, Instant end, HttpServletResponse response) {
    validatePeriod(start, end);

    List<EventParticipationExcelRow> rows = eventParticipationRepository.findExcelRows(eventId, start, end);

    List<List<String>> body = rows.stream()
        .map(row -> List.of(
            StringUtil.formatLocalDateTime(DateUtil.toAsiaSeoulDateTime(row.participationDt())),
            StringUtil.formatLocalDate(row.participationDate()),
            Integer.toString(row.dailyOrder()),
            StringUtil.defaultString(row.name()),
            StringUtil.defaultString(row.employeeNumber())))
        .toList();

    ExcelUtil.downloadXlsx(
        response,
        "event-participations.xlsx",
        PARTICIPATION_HEADERS,
        body);
  }

  private void validatePeriod(Instant start, Instant end) {
    if (start == null || end == null || start.isAfter(end)) {
      throw new IllegalArgumentException("유효한 기간이 필요합니다.");
    }
  }
}
