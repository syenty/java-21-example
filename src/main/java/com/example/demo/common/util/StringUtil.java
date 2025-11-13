package com.example.demo.common.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

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

  public static String randomNumericCode(int length) {
    if (length <= 0 || length > 9) {
      throw new IllegalArgumentException("length must be between 1 and 9");
    }
    int upperBound = (int) Math.pow(10, length);
    int value = ThreadLocalRandom.current().nextInt(0, upperBound);
    return String.format("%0" + length + "d", value);
  }
}
