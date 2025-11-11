package com.example.demo.participation.service.impl;

import com.example.demo.participation.domain.EventParticipation;
import com.example.demo.participation.domain.EventParticipationAnswer;
import com.example.demo.participation.dto.EventParticipationAnswerRequest;
import com.example.demo.participation.dto.EventParticipationAnswerResponse;
import com.example.demo.participation.repository.EventParticipationAnswerRepository;
import com.example.demo.participation.repository.EventParticipationRepository;
import com.example.demo.participation.service.EventParticipationAnswerService;
import com.example.demo.quiz.domain.Quiz;
import com.example.demo.quiz.domain.QuizOption;
import com.example.demo.quiz.repository.QuizOptionRepository;
import com.example.demo.quiz.repository.QuizRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventParticipationAnswerServiceImpl implements EventParticipationAnswerService {

  private final EventParticipationAnswerRepository eventParticipationAnswerRepository;
  private final EventParticipationRepository eventParticipationRepository;
  private final QuizRepository quizRepository;
  private final QuizOptionRepository optionRepository;

  @Override
  public List<EventParticipationAnswerResponse> findAll() {
    return eventParticipationAnswerRepository.findAll().stream()
        .map(EventParticipationAnswerResponse::of)
        .toList();
  }

  @Override
  public Optional<EventParticipationAnswerResponse> findById(Long id) {
    return eventParticipationAnswerRepository.findById(id).map(EventParticipationAnswerResponse::of);
  }

  @Override
  @Transactional
  public Optional<EventParticipationAnswerResponse> create(EventParticipationAnswerRequest request) {
    Optional<EventParticipation> participationOpt =
      eventParticipationRepository.findById(request.participationId());
    if (participationOpt.isEmpty()) {
      return Optional.empty();
    }
    Optional<Quiz> quizOpt = quizRepository.findById(request.quizId());
    if (quizOpt.isEmpty()) {
      return Optional.empty();
    }
    QuizOption option = null;
    if (request.optionId() != null) {
      option = optionRepository.findById(request.optionId()).orElse(null);
    }
    EventParticipationAnswer answer =
        EventParticipationAnswer.builder()
            .participation(participationOpt.get())
            .quiz(quizOpt.get())
            .option(option)
            .answerText(request.answerText())
            .correct(request.correct() != null && request.correct())
            .answerDt(request.answerDt() != null ? request.answerDt() : Instant.now())
            .build();
    return Optional.of(EventParticipationAnswerResponse.of(eventParticipationAnswerRepository.save(answer)));
  }

  @Override
  @Transactional
  public Optional<EventParticipationAnswerResponse> update(
      Long id, EventParticipationAnswerRequest request) {
    return eventParticipationAnswerRepository
        .findById(id)
        .flatMap(
            answer -> {
              if (request.participationId() != null
                  && !answer.getParticipation().getId().equals(request.participationId())) {
                Optional<EventParticipation> participationOpt =
                  eventParticipationRepository.findById(request.participationId());
                if (participationOpt.isEmpty()) {
                  return Optional.<EventParticipationAnswerResponse>empty();
                }
                answer.changeParticipation(participationOpt.get());
              }
              if (request.quizId() != null && !answer.getQuiz().getId().equals(request.quizId())) {
                Optional<Quiz> quizOpt = quizRepository.findById(request.quizId());
                if (quizOpt.isEmpty()) {
                  return Optional.<EventParticipationAnswerResponse>empty();
                }
                answer.changeQuiz(quizOpt.get());
              }
              QuizOption option = null;
              if (request.optionId() != null) {
                option = optionRepository.findById(request.optionId()).orElse(null);
              }
              answer.update(
                  request.answerText(), request.correct(), request.answerDt(), option);
              return Optional.of(EventParticipationAnswerResponse.of(answer));
            });
  }

  @Override
  @Transactional
  public boolean delete(Long id) {
    if (!eventParticipationAnswerRepository.existsById(id)) {
      return false;
    }
    eventParticipationAnswerRepository.deleteById(id);
    return true;
  }
}
