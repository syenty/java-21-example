package com.example.demo.quiz.service;

import com.example.demo.quiz.dto.QuizOptionRequest;
import com.example.demo.quiz.dto.QuizOptionResponse;
import java.util.List;
import java.util.Optional;

public interface QuizOptionService {
  List<QuizOptionResponse> findAll();

  Optional<QuizOptionResponse> findById(Long id);

  Optional<QuizOptionResponse> create(QuizOptionRequest request);

  Optional<QuizOptionResponse> update(Long id, QuizOptionRequest request);

  boolean delete(Long id);
}
