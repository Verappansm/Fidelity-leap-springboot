package com.example.dto;

public record ErrorResponse(
        String errorCode,
        String message
) {}
