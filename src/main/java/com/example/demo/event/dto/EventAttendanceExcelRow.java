package com.example.demo.event.dto;

import java.time.Instant;
import java.time.LocalDate;

public record EventAttendanceExcelRow(
    Instant attendanceDt,
    LocalDate attendanceDate,
    String userName,
    String employeeNumber) {}
