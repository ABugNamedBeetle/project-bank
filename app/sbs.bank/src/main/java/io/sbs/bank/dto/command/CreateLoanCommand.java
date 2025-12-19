package io.sbs.bank.dto.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateLoanCommand(
    @NotBlank(message = "Account number is required")
    String accountNumber,
    
    @NotNull(message = "Principal amount is required")
    @Positive(message = "Principal amount must be positive")
    BigDecimal principalAmount,
    
    @NotNull(message = "Interest rate is required")
    @Positive(message = "Interest rate must be positive")
    BigDecimal interestRate,
    
    @NotNull(message = "Tenure is required")
    @Positive(message = "Tenure must be positive")
    Integer tenureMonths
) {}

