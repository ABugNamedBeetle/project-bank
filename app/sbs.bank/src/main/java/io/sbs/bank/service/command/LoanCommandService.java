package io.sbs.bank.service.command;

import io.sbs.bank.dto.command.*;
import io.sbs.bank.entity.Loan;
import io.sbs.bank.entity.LoanStatus;
import io.sbs.bank.repository.AccountRepository;
import io.sbs.bank.repository.LoanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Service
public class LoanCommandService {
    
    private final LoanRepository loanRepository;
    private final AccountRepository accountRepository;
    
    public LoanCommandService(LoanRepository loanRepository, AccountRepository accountRepository) {
        this.loanRepository = loanRepository;
        this.accountRepository = accountRepository;
    }
    
    @Transactional
    public LoanCommandResponse createLoan(CreateLoanCommand command) {
        // Validate account exists
        accountRepository.findByAccountNumber(command.accountNumber())
                .orElseThrow(() -> new RuntimeException("Account not found: " + command.accountNumber()));
        
        // Calculate total amount with interest
        BigDecimal totalAmount = calculateTotalAmount(
                command.principalAmount(),
                command.interestRate(),
                command.tenureMonths()
        );
        
        Loan loan = new Loan();
        loan.setLoanNumber(generateLoanNumber());
        loan.setAccountNumber(command.accountNumber());
        loan.setPrincipalAmount(command.principalAmount());
        loan.setInterestRate(command.interestRate());
        loan.setTenureMonths(command.tenureMonths());
        loan.setTotalAmount(totalAmount);
        loan.setRemainingAmount(totalAmount);
        loan.setStatus(LoanStatus.PENDING);
        
        Loan savedLoan = loanRepository.save(loan);
        
        return new LoanCommandResponse(
                savedLoan.getLoanNumber(),
                "Loan application created successfully"
        );
    }
    
    @Transactional
    public LoanCommandResponse approveLoan(ApproveLoanCommand command) {
        Loan loan = loanRepository.findByLoanNumber(command.loanNumber())
                .orElseThrow(() -> new RuntimeException("Loan not found: " + command.loanNumber()));
        
        if (loan.getStatus() != LoanStatus.PENDING) {
            throw new RuntimeException("Only pending loans can be approved");
        }
        
        loan.setStatus(LoanStatus.APPROVED);
        loan.setApprovedAt(java.time.LocalDateTime.now());
        
        loanRepository.save(loan);
        
        return new LoanCommandResponse(
                loan.getLoanNumber(),
                "Loan approved successfully"
        );
    }
    
    @Transactional
    public LoanCommandResponse rejectLoan(RejectLoanCommand command) {
        Loan loan = loanRepository.findByLoanNumber(command.loanNumber())
                .orElseThrow(() -> new RuntimeException("Loan not found: " + command.loanNumber()));
        
        if (loan.getStatus() != LoanStatus.PENDING) {
            throw new RuntimeException("Only pending loans can be rejected");
        }
        
        loan.setStatus(LoanStatus.REJECTED);
        loan.setRejectedAt(java.time.LocalDateTime.now());
        loan.setRejectionReason(command.rejectionReason());
        
        loanRepository.save(loan);
        
        return new LoanCommandResponse(
                loan.getLoanNumber(),
                "Loan rejected"
        );
    }
    
    @Transactional
    public LoanCommandResponse repayLoan(RepayLoanCommand command) {
        Loan loan = loanRepository.findByLoanNumber(command.loanNumber())
                .orElseThrow(() -> new RuntimeException("Loan not found: " + command.loanNumber()));
        
        if (loan.getStatus() != LoanStatus.APPROVED && loan.getStatus() != LoanStatus.ACTIVE) {
            throw new RuntimeException("Only approved or active loans can be repaid");
        }
        
        if (command.amount().compareTo(loan.getRemainingAmount()) > 0) {
            throw new RuntimeException("Repayment amount exceeds remaining loan amount");
        }
        
        BigDecimal newRemainingAmount = loan.getRemainingAmount().subtract(command.amount());
        loan.setRemainingAmount(newRemainingAmount);
        
        if (newRemainingAmount.compareTo(BigDecimal.ZERO) <= 0) {
            loan.setStatus(LoanStatus.CLOSED);
        } else {
            loan.setStatus(LoanStatus.ACTIVE);
        }
        
        loanRepository.save(loan);
        
        return new LoanCommandResponse(
                loan.getLoanNumber(),
                "Loan repayment processed successfully"
        );
    }
    
    private String generateLoanNumber() {
        return "LN" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    private BigDecimal calculateTotalAmount(BigDecimal principal, BigDecimal interestRate, Integer tenureMonths) {
        // Simple interest calculation: Total = Principal + (Principal * Rate * Time)
        BigDecimal interest = principal
                .multiply(interestRate)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(tenureMonths))
                .divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
        
        return principal.add(interest);
    }
}

