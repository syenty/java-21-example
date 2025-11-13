package com.example.demo.quiz.repository;

import com.example.demo.quiz.domain.Quiz;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
  List<Quiz> findByEvent_IdAndQuizDateOrderByQuestionOrderAsc(Long eventId, LocalDate quizDate);

  int countByEvent_IdAndQuizDate(Long eventId, LocalDate quizDate);

  Optional<Quiz> findByIdAndEvent_Id(Long id, Long eventId);

  @Query("""
      select q from Quiz q
      where (:quizDate is null or q.quizDate = :quizDate)
        and (:questionText is null or lower(q.questionText) like lower(concat('%', :questionText, '%')))
      order by q.id desc
      """)
  Page<Quiz> search(
      @Param("quizDate") LocalDate quizDate,
      @Param("questionText") String questionText,
      Pageable pageable);
}
