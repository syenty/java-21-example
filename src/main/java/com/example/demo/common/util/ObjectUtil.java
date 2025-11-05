package com.example.demo.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Slf4j
public class ObjectUtil {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  public static <T> boolean isEmpty(T data) {
    return ObjectUtils.isEmpty(data);
  }

  public static <T> boolean isNotEmpty(T data) {
    return !ObjectUtils.isEmpty(data);
  }

  public static <T> boolean isEmpty(List<T> data) {
    return ObjectUtils.isEmpty(data);
  }

  public static <T> boolean isNotEmpty(List<T> data) {
    return !ObjectUtils.isEmpty(data);
  }

  public static String splitAndLast(final String text,
      final String regex) {
    String[] array = text.split(regex);
    return array[array.length - 1];
  }

  public static <T> T fromJson(final String json,
      final Class<T> valueType) {
    try {
      return objectMapper.readValue(json, valueType);
    } catch (JsonProcessingException e) {
      log.error(e.getMessage());
      throw new RuntimeException("Could not parse json from string. Error: " + e.getMessage());
    }
  }

  public static String toJson(final Object data) {
    try {
      return objectMapper.writeValueAsString(data);
    } catch (JsonProcessingException e) {
      log.error(e.getMessage());
      throw new RuntimeException("Could not parse json to string. Error: " + e.getMessage());
    }
  }

  public static int calculateJsonLength(final Object frameData) {
    if (frameData == null) {
      return 0;
    }
    if (frameData instanceof String frameDataString) {
      return frameDataString.length();
    }
    String json = ObjectUtil.toJson(frameData);
    return json.length();
  }

  public static String saveAsJson(final String storagePath, final Object data) {
    LocalDate today = LocalDate.now();
    String datePath = today.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    String fileName = UUID.randomUUID().toString() + ".json";
    String relativePath = Paths.get(datePath, fileName).toString();
    Path path = Paths.get(storagePath, relativePath);

    try {
      // 디렉토리가 없으면 생성
      Files.createDirectories(path.getParent());
      // 파일에 JSON 데이터 쓰기
      Files.writeString(path, toJson(data));
    } catch (IOException e) {
      log.error("Could not store the file. Error: " + e.getMessage());
      // 파일 생성 실패 시 예외 처리
      throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
    }
    return relativePath;
  }

}
