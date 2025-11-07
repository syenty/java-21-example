package com.example.demo.quiz.service.impl;

import com.example.demo.event.repository.EventRepository;
import com.example.demo.participation.dto.QuizParticipationRequest;
import com.example.demo.quiz.domain.Quiz;
import com.example.demo.quiz.domain.QuizOption;
import com.example.demo.quiz.dto.QuizRequest;
import com.example.demo.quiz.dto.QuizResponse;
import com.example.demo.quiz.repository.QuizOptionRepository;
import com.example.demo.quiz.repository.QuizRepository;
import com.example.demo.quiz.service.QuizService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuizServiceImpl implements QuizService {

  private final QuizRepository quizRepository;
  private final EventRepository eventRepository;
  private final QuizOptionRepository quizOptionRepository;

  @Override
  public List<QuizResponse> findAll() {
    return quizRepository.findAll().stream().map(QuizResponse::of).toList();
  }

  @Override
  public Optional<QuizResponse> findById(Long id) {
    return quizRepository.findById(id).map(QuizResponse::of);
  }

  @Override
  @Transactional
  public Optional<QuizResponse> create(QuizRequest request) {
    if (request.quizDate() == null) {
      throw new IllegalArgumentException("quizDate is required");
    }
    return eventRepository
        .findById(request.eventId())
        .map(
            event -> {
              Quiz quiz =
                  Quiz.builder()
                      .event(event)
                      .type(request.type())
                      .questionText(request.questionText())
                      .correctText(request.correctText())
                      .quizDate(request.quizDate())
                      .questionOrder(
                          request.questionOrder() != null ? request.questionOrder() : 1)
                      .active(request.active() != null ? request.active() : true)
                      .build();
              return QuizResponse.of(quizRepository.save(quiz));
            });
  }

  @Override
  @Transactional
  public Optional<QuizResponse> update(Long id, QuizRequest request) {
    return quizRepository
        .findById(id)
        .flatMap(
            quiz -> {
              Long targetEventId =
                  request.eventId() != null ? request.eventId() : quiz.getEvent().getId();
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
                        return QuizResponse.of(quiz);
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
  public List<QuizResponse> findByEventAndDate(Long eventId, LocalDate quizDate) {
    return quizRepository.findByEvent_IdAndQuizDateOrderByQuestionOrderAsc(eventId, quizDate).stream()
        .map(QuizResponse::of)
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
  public boolean areAllAnswersCorrect(
      Long eventId, List<QuizParticipationRequest.QuizAnswer> answers) {
    if (answers == null || answers.isEmpty()) {
      return false;
    }

    for (QuizParticipationRequest.QuizAnswer answer : answers) {
      if (answer == null || answer.quizId() == null) {
        return false;
      }

      Quiz quiz =
          quizRepository
              .findByIdAndEvent_Id(answer.quizId(), eventId)
              .orElse(null);

      if (quiz == null) {
        return false;
      }

      if (answer.optionId() != null) {
        QuizOption option =
            quizOptionRepository.findById(answer.optionId()).orElse(null);
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
