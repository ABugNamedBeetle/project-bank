package io.sbs.bank.repository;

import io.sbs.bank.entity.Loan;
import io.sbs.bank.entity.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    Optional<Loan> findByLoanNumber(String loanNumber);
    List<Loan> findByAccountNumber(String accountNumber);
    List<Loan> findByStatus(LoanStatus status);
    
    @Query("SELECT l FROM Loan l WHERE l.accountNumber = :accountNumber AND l.status = :status")
    List<Loan> findByAccountNumberAndStatus(@Param("accountNumber") String accountNumber, 
                                             @Param("status") LoanStatus status);
}

