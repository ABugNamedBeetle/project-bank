package io.sbs.bank.dto.command;

public record LoanCommandResponse(
    String loanNumber,
    String message
) {}

