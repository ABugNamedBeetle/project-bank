package io.sbs.bank.service.query;

import io.sbs.bank.dto.query.LoanListResponse;
import io.sbs.bank.dto.query.LoanResponse;
import io.sbs.bank.entity.Loan;
import io.sbs.bank.entity.LoanStatus;
import io.sbs.bank.repository.LoanRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanQueryService {
    
    private static final String LOANS_CACHE = "loans";
    
    private final LoanRepository loanRepository;
    
    public LoanQueryService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }
    
    @Cacheable(value = LOANS_CACHE, key = "#loanNumber")
    public LoanResponse getLoanByLoanNumber(String loanNumber) {
        Loan loan = loanRepository.findByLoanNumber(loanNumber)
                .orElseThrow(() -> new RuntimeException("Loan not found: " + loanNumber));
        return mapToResponse(loan);
    }
    
    public LoanListResponse getLoansByAccountNumber(String accountNumber) {
        List<Loan> loans = loanRepository.findByAccountNumber(accountNumber);
        List<LoanResponse> loanResponses = loans.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return new LoanListResponse(loanResponses, loanResponses.size());
    }
    
    public LoanListResponse getLoansByStatus(LoanStatus status) {
        List<Loan> loans = loanRepository.findByStatus(status);
        List<LoanResponse> loanResponses = loans.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return new LoanListResponse(loanResponses, loanResponses.size());
    }
    
    public LoanListResponse getAllLoans() {
        List<Loan> loans = loanRepository.findAll();
        List<LoanResponse> loanResponses = loans.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return new LoanListResponse(loanResponses, loanResponses.size());
    }
    
    private LoanResponse mapToResponse(Loan loan) {
        return new LoanResponse(
                loan.getId(),
                loan.getLoanNumber(),
                loan.getAccountNumber(),
                loan.getPrincipalAmount(),
                loan.getInterestRate(),
                loan.getTenureMonths(),
                loan.getStatus(),
                loan.getRemainingAmount(),
                loan.getTotalAmount(),
                loan.getCreatedAt(),
                loan.getApprovedAt(),
                loan.getRejectedAt(),
                loan.getRejectionReason()
        );
    }
}

