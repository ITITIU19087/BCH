package com.antnest.bch.dto;

import lombok.Builder;

import java.sql.Time;

@Builder
public class ErrorResponseDto {
    private String message;
    private Time time;
}
