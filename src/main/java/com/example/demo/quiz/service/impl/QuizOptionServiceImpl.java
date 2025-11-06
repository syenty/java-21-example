package com.example.demo.quiz.service.impl;

import com.example.demo.quiz.domain.QuizOption;
import com.example.demo.quiz.dto.QuizOptionRequest;
import com.example.demo.quiz.dto.QuizOptionResponse;
import com.example.demo.quiz.repository.QuizOptionRepository;
import com.example.demo.quiz.repository.QuizRepository;
import com.example.demo.quiz.service.QuizOptionService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuizOptionServiceImpl implements QuizOptionService {

  private final QuizOptionRepository optionRepository;
  private final QuizRepository quizRepository;

  @Override
  public List<QuizOptionResponse> findAll() {
    return optionRepository.findAll().stream().map(QuizOptionResponse::of).toList();
  }

  @Override
  public Optional<QuizOptionResponse> findById(Long id) {
    return optionRepository.findById(id).map(QuizOptionResponse::of);
  }

  @Override
  @Transactional
  public Optional<QuizOptionResponse> create(QuizOptionRequest request) {
    return quizRepository
        .findById(request.quizId())
        .map(
            quiz -> {
              QuizOption option =
                  QuizOption.builder()
                      .quiz(quiz)
                      .optionKey(request.optionKey())
                      .optionText(request.optionText())
                      .correct(request.correct() != null ? request.correct() : Boolean.FALSE)
                      .optionOrder(
                          request.optionOrder() != null ? request.optionOrder() : 1)
                      .build();
              return QuizOptionResponse.of(optionRepository.save(option));
            });
  }

  @Override
  @Transactional
  public Optional<QuizOptionResponse> update(Long id, QuizOptionRequest request) {
    return optionRepository
        .findById(id)
        .flatMap(
            option -> {
              if (request.quizId() != null
                  && !option.getQuiz().getId().equals(request.quizId())) {
                return quizRepository
                    .findById(request.quizId())
                    .map(
                        quiz -> {
                          option.changeQuiz(quiz);
                          option.update(
                              request.optionKey(),
                              request.optionText(),
                              request.correct(),
                              request.optionOrder());
                          return QuizOptionResponse.of(option);
                        });
              }
              option.update(
                  request.optionKey(),
                  request.optionText(),
                  request.correct(),
                  request.optionOrder());
              return Optional.of(QuizOptionResponse.of(option));
            });
  }

  @Override
  @Transactional
  public boolean delete(Long id) {
    if (!optionRepository.existsById(id)) {
      return false;
    }
    optionRepository.deleteById(id);
    return true;
  }
}
