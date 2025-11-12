package com.example.demo.common.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class StringUtil {

  private static final DateTimeFormatter ISO_INSTANT_FORMATTER = DateTimeFormatter.ISO_INSTANT;

  private StringUtil() {
    throw new UnsupportedOperationException("Utility class");
  }

  public static String formatInstant(Instant instant) {
    return instant != null ? ISO_INSTANT_FORMATTER.format(instant) : "";
  }

  public static String formatLocalDateTime(LocalDateTime dateTime) {
    return dateTime != null ? dateTime.toString() : "";
  }

  public static String formatLocalDate(LocalDate date) {
    return date != null ? date.toString() : "";
  }

  public static String defaultString(String value) {
    return value != null ? value : "";
  }
}
