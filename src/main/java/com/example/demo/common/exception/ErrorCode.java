package com.example.demo.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

  INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C999", "서버 에러가 발생했습니다."),

  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "존재하지 않는 사용자입니다."),
  USER_BLOCKED(HttpStatus.FORBIDDEN, "U002", "차단된 사용자입니다."),

  EVENT_NOT_FOUND(HttpStatus.NOT_FOUND, "E001", "존재하지 않는 이벤트입니다."),
  EVENT_PARTICIPATION_TIME_REQUIRED(HttpStatus.BAD_REQUEST, "E002", "참여 가능 시간은 반드시 입력되어야 합니다."),
  EVENT_ID_REQUIRED(HttpStatus.BAD_REQUEST, "E003", "eventId는 필수입니다."),
  EVENT_NOT_ACTIVE(HttpStatus.BAD_REQUEST, "E004", "진행 중인 이벤트가 아닙니다."),
  EVENT_PERIOD_INVALID(HttpStatus.BAD_REQUEST, "E005", "이벤트 기간이 아닙니다."),
  EVENT_PARTICIPATION_WINDOW_CLOSED(HttpStatus.BAD_REQUEST, "E006", "지정된 참여 시간이 아닙니다."),

  AUTH_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "A001", "인증이 필요합니다."),
  AUTH_FORBIDDEN(HttpStatus.FORBIDDEN, "A002", "접근 권한이 없습니다."),
  AUTH_INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "A003", "유효하지 않은 토큰입니다."),
  AUTH_BAD_CREDENTIALS(HttpStatus.UNAUTHORIZED, "A004", "아이디 또는 비밀번호가 올바르지 않습니다."),

  QUIZ_DATE_REQUIRED(HttpStatus.BAD_REQUEST, "Q001", "퀴즈 일자는 필수입니다."),
  QUIZ_INFO_INVALID(HttpStatus.BAD_REQUEST, "Q002", "퀴즈 정보가 올바르지 않습니다."),
  QUIZ_NOT_FOUND(HttpStatus.NOT_FOUND, "Q003", "존재하지 않는 퀴즈입니다."),
  QUIZ_EVENT_MISMATCH(HttpStatus.BAD_REQUEST, "Q004", "해당 이벤트의 퀴즈만 응답할 수 있습니다."),
  QUIZ_OPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "Q005", "존재하지 않는 선택지입니다."),
  QUIZ_OPTION_MISMATCH(HttpStatus.BAD_REQUEST, "Q006", "퀴즈와 선택지가 일치하지 않습니다."),
  QUIZ_ANSWER_INCORRECT(HttpStatus.BAD_REQUEST, "Q007", "퀴즈 정답을 맞춰야 참여가 인정됩니다."),

  PARTICIPATION_ALREADY_DONE(HttpStatus.CONFLICT, "P001", "이미 오늘 참여했습니다."),

  REWARD_POLICY_TYPE_REQUIRED(HttpStatus.BAD_REQUEST, "R001", "정책 유형은 필수입니다."),
  REWARD_POLICY_TARGET_ORDER_INVALID(HttpStatus.BAD_REQUEST, "R002", "순서에 적절한 값이 필요합니다."),
  REWARD_POLICY_NTH_SCOPE_REQUIRED(HttpStatus.BAD_REQUEST, "R003", "순번 정책에 필수적인 값입니다.");

  private final HttpStatus status;
  private final String code;
  private final String message;

}
