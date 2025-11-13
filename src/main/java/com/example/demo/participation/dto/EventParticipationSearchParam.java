package com.example.demo.participation.dto;

import com.example.demo.common.dto.PageParam;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class EventParticipationSearchParam extends PageParam {

  @NotNull
  private Long eventId;

  @NotBlank
  private String startDt;

  @NotBlank
  private String endDt;
}
