package com.example.demo.event.dto;

import com.example.demo.common.dto.PageParam;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class EventAttendanceSearchParam extends PageParam {

  @NotBlank
  private String startDt;

  @NotBlank
  private String endDt;
}
