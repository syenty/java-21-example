package com.example.demo.quiz.service.impl;

import com.example.demo.event.repository.EventRepository;
import com.example.demo.quiz.domain.Quiz;
import com.example.demo.quiz.dto.QuizRequest;
import com.example.demo.quiz.dto.QuizResponse;
import com.example.demo.quiz.repository.QuizRepository;
import com.example.demo.quiz.service.QuizService;
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
}
