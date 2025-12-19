package io.sbs.bank.controller;

import io.sbs.bank.dto.CreateAccountRequest;
import io.sbs.bank.dto.DepositRequest;
import io.sbs.bank.dto.TransferRequest;
import io.sbs.bank.dto.TransferResponse;
import io.sbs.bank.entity.Account;
import io.sbs.bank.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    
    private final AccountService accountService;
    
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }
    
    @PostMapping
    public ResponseEntity<Account> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        Account account = accountService.createAccount(request.ownerName());
        return ResponseEntity.ok(account);
    }
    
    @GetMapping("/{accountNumber}")
    public ResponseEntity<Account> getAccount(@PathVariable String accountNumber) {
        return ResponseEntity.ok(accountService.getAccount(accountNumber));
    }
    
    @PostMapping("/{accountNumber}/deposit")
    public ResponseEntity<Account> deposit(
            @PathVariable String accountNumber,
            @Valid @RequestBody DepositRequest request) {
        Account account = accountService.deposit(accountNumber, request.amount());
        return ResponseEntity.ok(account);
    }
    
    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transfer(@Valid @RequestBody TransferRequest request) {
        accountService.transfer(request.fromAccount(), request.toAccount(), request.amount());
        return ResponseEntity.ok(new TransferResponse("Transfer successful"));
    }
}

