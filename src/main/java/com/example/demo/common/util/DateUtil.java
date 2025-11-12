package com.example.demo.common.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public final class DateUtil {

  private static final ZoneId ASIA_SEOUL = ZoneId.of("Asia/Seoul");
  private static final DateTimeFormatter REQUEST_DATE_TIME_FORMATTER = DateTimeFormatter
      .ofPattern("yyyy-MM-dd HH:mm:ss");

  private DateUtil() {
    throw new UnsupportedOperationException("Utility class");
  }

  public static ZonedDateTime nowUtc() {
    return ZonedDateTime.now(ZoneOffset.UTC);
  }

  public static Instant currentInstantUtc() {
    return nowUtc().toInstant();
  }

  public static LocalDateTime toAsiaSeoulDateTime(Instant instant) {
    return instant.atZone(ASIA_SEOUL).toLocalDateTime();
  }

  public static LocalDate toAsiaSeoulDate(Instant instant) {
    return instant.atZone(ASIA_SEOUL).toLocalDate();
  }

  public static LocalDate utcToAsiaSeoulDate(ZonedDateTime utcDateTime) {
    return utcDateTime.withZoneSameInstant(ASIA_SEOUL).toLocalDate();
  }

  public static Instant parseUtcDateTime(String value) {
    try {
      LocalDateTime localDateTime = LocalDateTime.parse(value, REQUEST_DATE_TIME_FORMATTER);
      return localDateTime.toInstant(ZoneOffset.UTC);
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("날짜 형식은 yyyy-MM-dd HH:mm:ss (UTC) 이어야 합니다.", e);
    }
  }
}
