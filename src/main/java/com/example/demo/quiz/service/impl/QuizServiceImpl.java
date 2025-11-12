package com.example.demo.quiz.service.impl;

import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.event.repository.EventRepository;
import com.example.demo.participation.dto.EventParticipationRequest;
import com.example.demo.quiz.domain.Quiz;
import com.example.demo.quiz.domain.QuizOption;
import com.example.demo.quiz.dto.QuizAdminResponse;
import com.example.demo.quiz.dto.QuizOptionResponse;
import com.example.demo.quiz.dto.QuizOptionUserResponse;
import com.example.demo.quiz.dto.QuizRequest;
import com.example.demo.quiz.dto.QuizUserResponse;
import com.example.demo.quiz.repository.QuizOptionRepository;
import com.example.demo.quiz.repository.QuizRepository;
import com.example.demo.quiz.service.QuizService;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuizServiceImpl implements QuizService {

  private static final ZoneId DEFAULT_ZONE_ID = ZoneId.of("Asia/Seoul");
  private final QuizRepository quizRepository;
  private final EventRepository eventRepository;
  private final QuizOptionRepository quizOptionRepository;

  @Override
  public List<QuizAdminResponse> findAll() {
    List<Quiz> quizzes = quizRepository.findAll();
    Map<Long, List<QuizOptionResponse>> optionMap = groupAdminOptionsByQuizIds(quizzes);
    return quizzes.stream()
        .map(quiz -> QuizAdminResponse.of(quiz, optionMap.getOrDefault(quiz.getId(), List.of())))
        .toList();
  }

  @Override
  public Optional<QuizAdminResponse> findById(Long id) {
    return quizRepository
        .findById(id)
        .map(quiz -> QuizAdminResponse.of(quiz, loadAdminOptions(quiz.getId())));
  }

  @Override
  @Transactional
  public Optional<QuizAdminResponse> create(QuizRequest request) {
    if (request.quizDate() == null) {
      throw new BusinessException(ErrorCode.QUIZ_DATE_REQUIRED);
    }
    return eventRepository
        .findById(request.eventId())
        .map(
            event -> {
              Quiz quiz = Quiz.builder()
                  .event(event)
                  .type(request.type())
                  .questionText(request.questionText())
                  .correctText(request.correctText())
                  .quizDate(request.quizDate())
                  .questionOrder(
                      request.questionOrder() != null ? request.questionOrder() : 1)
                  .active(request.active() != null ? request.active() : true)
                  .build();
              Quiz saved = quizRepository.save(quiz);
              return QuizAdminResponse.of(saved, Collections.emptyList());
            });
  }

  @Override
  @Transactional
  public Optional<QuizAdminResponse> update(Long id, QuizRequest request) {
    return quizRepository
        .findById(id)
        .flatMap(
            quiz -> {
              Long targetEventId = request.eventId() != null ? request.eventId() : quiz.getEvent().getId();
              return eventRepository
                  .findById(targetEventId)
                  .map(
                      event -> {
                        if (!quiz.getEvent().getId().equals(event.getId())) {
                          quiz.changeEvent(event);
                        }
                        quiz.update(
                            request.type(),
                            request.questionText(),
                            request.correctText(),
                            request.quizDate(),
                            request.questionOrder(),
                            request.active());
                        return QuizAdminResponse.of(quiz, loadAdminOptions(quiz.getId()));
                      });
            });
  }

  @Override
  @Transactional
  public boolean delete(Long id) {
    if (!quizRepository.existsById(id)) {
      return false;
    }
    quizRepository.deleteById(id);
    return true;
  }

  @Override
  public List<QuizUserResponse> findByEventAndDate(Long eventId, LocalDate quizDate) {
    List<Quiz> quizzes = quizRepository.findByEvent_IdAndQuizDateOrderByQuestionOrderAsc(eventId, quizDate);
    Map<Long, List<QuizOptionUserResponse>> optionMap = groupUserOptionsByQuizIds(quizzes);
    return quizzes.stream()
        .map(quiz -> QuizUserResponse.of(quiz, optionMap.getOrDefault(quiz.getId(), List.of())))
        .toList();
  }

  @Override
  public List<QuizUserResponse> findTodayByEvent(Long eventId) {
    LocalDate today = LocalDate.now(DEFAULT_ZONE_ID);
    return findByEventAndDate(eventId, today);
  }

  private Map<Long, List<QuizOptionResponse>> groupAdminOptionsByQuizIds(List<Quiz> quizzes) {
    if (quizzes == null || quizzes.isEmpty()) {
      return Collections.emptyMap();
    }
    List<Long> quizIds = quizzes.stream().map(Quiz::getId).toList();
    return quizOptionRepository
        .findByQuiz_IdInOrderByQuiz_IdAscOptionOrderAsc(quizIds)
        .stream()
        .map(QuizOptionResponse::of)
        .collect(Collectors.groupingBy(QuizOptionResponse::quizId));
  }

  private Map<Long, List<QuizOptionUserResponse>> groupUserOptionsByQuizIds(List<Quiz> quizzes) {
    if (quizzes == null || quizzes.isEmpty()) {
      return Collections.emptyMap();
    }
    List<Long> quizIds = quizzes.stream().map(Quiz::getId).toList();
    return quizOptionRepository
        .findByQuiz_IdInOrderByQuiz_IdAscOptionOrderAsc(quizIds)
        .stream()
        .map(QuizOptionUserResponse::of)
        .collect(Collectors.groupingBy(QuizOptionUserResponse::quizId));
  }

  private List<QuizOptionResponse> loadAdminOptions(Long quizId) {
    if (quizId == null) {
      return Collections.emptyList();
    }
    return quizOptionRepository.findByQuiz_IdOrderByOptionOrderAsc(quizId).stream()
        .map(QuizOptionResponse::of)
        .toList();
  }

  @Override
  public boolean isCorrectAnswer(Long quizId, Long eventId, String answer) {
    return quizRepository
        .findByIdAndEvent_Id(quizId, eventId)
        .map(quiz -> quiz.getCorrectText() != null && quiz.getCorrectText().equalsIgnoreCase(answer))
        .orElse(false);
  }

  @Override
  public boolean areAllAnswersCorrect(Long eventId, List<EventParticipationRequest.QuizAnswer> answers) {
    if (answers == null || answers.isEmpty()) {
      return false;
    }

    for (EventParticipationRequest.QuizAnswer answer : answers) {
      if (answer == null || answer.quizId() == null) {
        return false;
      }

      Quiz quiz = quizRepository
          .findByIdAndEvent_Id(answer.quizId(), eventId)
          .orElse(null);

      if (quiz == null) {
        return false;
      }

      if (answer.optionId() != null) {
        QuizOption option = quizOptionRepository.findById(answer.optionId()).orElse(null);
        if (option == null || !option.getQuiz().getId().equals(quiz.getId()) || !option.isCorrect()) {
          return false;
        }
      } else if (answer.answerText() != null) {
        String submitted = answer.answerText().trim();
        if (quiz.getCorrectText() == null
            || submitted.isEmpty()
            || !quiz.getCorrectText().equalsIgnoreCase(submitted)) {
          return false;
        }
      } else {
        return false;
      }
    }

    return true;
  }
}
