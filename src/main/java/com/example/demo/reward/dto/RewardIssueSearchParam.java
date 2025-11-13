package com.example.demo.reward.dto;

import com.example.demo.common.dto.PageParam;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RewardIssueSearchParam extends PageParam {

  @NotNull
  private Long eventId;

  @NotBlank
  private String startDt;

  @NotBlank
  private String endDt;
}
