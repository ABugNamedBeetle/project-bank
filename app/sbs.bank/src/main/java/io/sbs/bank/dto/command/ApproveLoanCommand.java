package io.sbs.bank.dto.command;

import jakarta.validation.constraints.NotBlank;

public record ApproveLoanCommand(
    @NotBlank(message = "Loan number is required")
    String loanNumber
) {}

