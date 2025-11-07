package com.example.demo.quiz.repository;

import com.example.demo.quiz.domain.Quiz;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
  List<Quiz> findByEvent_IdAndQuizDateOrderByQuestionOrderAsc(Long eventId, LocalDate quizDate);

  Optional<Quiz> findByIdAndEvent_Id(Long id, Long eventId);
}
