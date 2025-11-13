package com.example.demo.quiz.dto;

import com.example.demo.common.dto.PageParam;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizSearchParam extends PageParam {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate quizDate;

  private String questionText;
}
