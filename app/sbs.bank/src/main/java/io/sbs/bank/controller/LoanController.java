package io.sbs.bank.controller;

import io.sbs.bank.dto.command.*;
import io.sbs.bank.dto.query.LoanListResponse;
import io.sbs.bank.dto.query.LoanResponse;
import io.sbs.bank.entity.LoanStatus;
import io.sbs.bank.service.command.LoanCommandService;
import io.sbs.bank.service.query.LoanQueryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loans")
public class LoanController {
    
    private final LoanCommandService commandService;
    private final LoanQueryService queryService;
    
    public LoanController(LoanCommandService commandService, LoanQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }
    
    // Command Endpoints (Write Operations)
    @PostMapping("/commands/create")
    public ResponseEntity<LoanCommandResponse> createLoan(@Valid @RequestBody CreateLoanCommand command) {
        return ResponseEntity.ok(commandService.createLoan(command));
    }
    
    @PostMapping("/commands/approve")
    public ResponseEntity<LoanCommandResponse> approveLoan(@Valid @RequestBody ApproveLoanCommand command) {
        return ResponseEntity.ok(commandService.approveLoan(command));
    }
    
    @PostMapping("/commands/reject")
    public ResponseEntity<LoanCommandResponse> rejectLoan(@Valid @RequestBody RejectLoanCommand command) {
        return ResponseEntity.ok(commandService.rejectLoan(command));
    }
    
    @PostMapping("/commands/repay")
    public ResponseEntity<LoanCommandResponse> repayLoan(@Valid @RequestBody RepayLoanCommand command) {
        return ResponseEntity.ok(commandService.repayLoan(command));
    }
    
    // Query Endpoints (Read Operations)
    @GetMapping("/queries/{loanNumber}")
    public ResponseEntity<LoanResponse> getLoan(@PathVariable String loanNumber) {
        return ResponseEntity.ok(queryService.getLoanByLoanNumber(loanNumber));
    }
    
    @GetMapping("/queries/account/{accountNumber}")
    public ResponseEntity<LoanListResponse> getLoansByAccount(@PathVariable String accountNumber) {
        return ResponseEntity.ok(queryService.getLoansByAccountNumber(accountNumber));
    }
    
    @GetMapping("/queries/status/{status}")
    public ResponseEntity<LoanListResponse> getLoansByStatus(@PathVariable LoanStatus status) {
        return ResponseEntity.ok(queryService.getLoansByStatus(status));
    }
    
    @GetMapping("/queries/all")
    public ResponseEntity<LoanListResponse> getAllLoans() {
        return ResponseEntity.ok(queryService.getAllLoans());
    }
}

