package io.sbs.bank.dto.query;

import io.sbs.bank.entity.LoanStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record LoanResponse(
    Long id,
    String loanNumber,
    String accountNumber,
    BigDecimal principalAmount,
    BigDecimal interestRate,
    Integer tenureMonths,
    LoanStatus status,
    BigDecimal remainingAmount,
    BigDecimal totalAmount,
    LocalDateTime createdAt,
    LocalDateTime approvedAt,
    LocalDateTime rejectedAt,
    String rejectionReason
) {}

