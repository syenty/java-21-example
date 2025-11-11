package com.example.demo.common.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public final class DateUtil {

  private static final ZoneId ASIA_SEOUL = ZoneId.of("Asia/Seoul");

  private DateUtil() {
    throw new UnsupportedOperationException("Utility class");
  }

  public static ZonedDateTime nowUtc() {
    return ZonedDateTime.now(ZoneOffset.UTC);
  }

  public static Instant currentInstantUtc() {
    return nowUtc().toInstant();
  }

  public static LocalDate toAsiaSeoulDate(Instant instant) {
    return instant.atZone(ASIA_SEOUL).toLocalDate();
  }

  public static LocalDate utcToAsiaSeoulDate(ZonedDateTime utcDateTime) {
    return utcDateTime.withZoneSameInstant(ASIA_SEOUL).toLocalDate();
  }
}
