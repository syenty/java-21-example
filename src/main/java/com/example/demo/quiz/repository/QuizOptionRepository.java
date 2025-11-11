package com.example.demo.quiz.repository;

import com.example.demo.quiz.domain.QuizOption;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizOptionRepository extends JpaRepository<QuizOption, Long> {
  List<QuizOption> findByQuiz_IdOrderByOptionOrderAsc(Long quizId);

  List<QuizOption> findByQuiz_IdInOrderByQuiz_IdAscOptionOrderAsc(List<Long> quizIds);
}
