package com.example.demo.user.dto;

import com.example.demo.common.dto.PageParam;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSearchParam extends PageParam {

  private String name;
  private String employeeNumber;
}
