package com.banking.bankingsystem.controller;

import com.banking.bankingsystem.dto.CreateAccountRequest;
import com.banking.bankingsystem.dto.DepositRequest;
import com.banking.bankingsystem.model.Account;
import com.banking.bankingsystem.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        Account account = accountService.createAccount(request);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable Long id) {
        Account account = accountService.getAccount(id);
        return ResponseEntity.ok(account);
    }

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<Void> withdraw(@PathVariable Long id, @Valid @RequestBody DepositRequest request) {
        accountService.withdraw(id, request.getAmount());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<Void> deposit(@PathVariable Long id, @Valid @RequestBody DepositRequest request) {
        accountService.deposit(id, request.getAmount());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{fromAccountId}/transfer/{toAccountId}")
    public ResponseEntity<Void> transfer(@PathVariable Long fromAccountId, @PathVariable Long toAccountId, @Valid @RequestBody DepositRequest request){
        accountService.transfer(fromAccountId, toAccountId, request.getAmount());
        return ResponseEntity.ok().build();
    }

}