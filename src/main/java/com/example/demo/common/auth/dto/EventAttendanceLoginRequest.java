package com.example.demo.common.auth.dto;

import lombok.Getter;

@Getter
public class EventAttendanceLoginRequest {
    private Long eventId;
    private String employeeNumber;
    private String name;
}
