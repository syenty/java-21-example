package com.example.demo.participation.service.impl;

import com.example.demo.participation.domain.QuizParticipation;
import com.example.demo.participation.domain.QuizParticipationAnswer;
import com.example.demo.participation.dto.QuizParticipationAnswerRequest;
import com.example.demo.participation.dto.QuizParticipationAnswerResponse;
import com.example.demo.participation.repository.QuizParticipationAnswerRepository;
import com.example.demo.participation.repository.QuizParticipationRepository;
import com.example.demo.participation.service.QuizParticipationAnswerService;
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
public class QuizParticipationAnswerServiceImpl implements QuizParticipationAnswerService {

  private final QuizParticipationAnswerRepository answerRepository;
  private final QuizParticipationRepository participationRepository;
  private final QuizRepository quizRepository;
  private final QuizOptionRepository optionRepository;

  @Override
  public List<QuizParticipationAnswerResponse> findAll() {
    return answerRepository.findAll().stream()
        .map(QuizParticipationAnswerResponse::of)
        .toList();
  }

  @Override
  public Optional<QuizParticipationAnswerResponse> findById(Long id) {
    return answerRepository.findById(id).map(QuizParticipationAnswerResponse::of);
  }

  @Override
  @Transactional
  public Optional<QuizParticipationAnswerResponse> create(QuizParticipationAnswerRequest request) {
    Optional<QuizParticipation> participationOpt =
        participationRepository.findById(request.participationId());
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
    QuizParticipationAnswer answer =
        QuizParticipationAnswer.builder()
            .participation(participationOpt.get())
            .quiz(quizOpt.get())
            .option(option)
            .answerText(request.answerText())
            .correct(request.correct() != null && request.correct())
            .answeredDt(request.answeredDt() != null ? request.answeredDt() : Instant.now())
            .build();
    return Optional.of(QuizParticipationAnswerResponse.of(answerRepository.save(answer)));
  }

  @Override
  @Transactional
  public Optional<QuizParticipationAnswerResponse> update(
      Long id, QuizParticipationAnswerRequest request) {
    return answerRepository
        .findById(id)
        .flatMap(
            answer -> {
              if (request.participationId() != null
                  && !answer.getParticipation().getId().equals(request.participationId())) {
                Optional<QuizParticipation> participationOpt =
                    participationRepository.findById(request.participationId());
                if (participationOpt.isEmpty()) {
                  return Optional.<QuizParticipationAnswerResponse>empty();
                }
                answer.changeParticipation(participationOpt.get());
              }
              if (request.quizId() != null && !answer.getQuiz().getId().equals(request.quizId())) {
                Optional<Quiz> quizOpt = quizRepository.findById(request.quizId());
                if (quizOpt.isEmpty()) {
                  return Optional.<QuizParticipationAnswerResponse>empty();
                }
                answer.changeQuiz(quizOpt.get());
              }
              QuizOption option = null;
              if (request.optionId() != null) {
                option = optionRepository.findById(request.optionId()).orElse(null);
              }
              answer.update(
                  request.answerText(), request.correct(), request.answeredDt(), option);
              return Optional.of(QuizParticipationAnswerResponse.of(answer));
            });
  }

  @Override
  @Transactional
  public boolean delete(Long id) {
    if (!answerRepository.existsById(id)) {
      return false;
    }
    answerRepository.deleteById(id);
    return true;
  }
}
