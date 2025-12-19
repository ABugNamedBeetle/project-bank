package io.sbs.bank.dto;

public record ValidationError(
    String field,
    String message,
    Object rejectedValue
) {}

