package io.sbs.bank.dto.query;

import java.util.List;

public record LoanListResponse(
    List<LoanResponse> loans,
    int totalCount
) {}

