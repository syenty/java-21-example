package com.example.demo.quiz.repository;

import com.example.demo.quiz.domain.QuizOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizOptionRepository extends JpaRepository<QuizOption, Long> {
}
