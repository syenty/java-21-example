package com.example.demo.participation.dto;

import java.time.Instant;
import java.time.LocalDate;

public record EventParticipationExcelRow(
    Instant participationDt,
    LocalDate participationDate,
    int dailyOrder,
    String name,
    String employeeNumber) {}
