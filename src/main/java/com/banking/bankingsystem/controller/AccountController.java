package com.banking.bankingsystem.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.bankingsystem.dto.CreateAccountRequest;
import com.banking.bankingsystem.dto.DepositRequest;
import com.banking.bankingsystem.dto.UpdateAccountRequest;
import com.banking.bankingsystem.model.Account;
import com.banking.bankingsystem.model.Transaction;
import com.banking.bankingsystem.service.AccountService;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:4200", methods = {
        RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH, RequestMethod.DELETE
})
@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        Account account = accountService.createAccount(request);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<Account> getAccount(@PathVariable String accountNumber) {
        Account account = accountService.getAccount(accountNumber);
        return ResponseEntity.ok(account);
    }

    @PostMapping("/{accountNumber}/withdraw")
    public ResponseEntity<Void> withdraw(@PathVariable String accountNumber, @Valid @RequestBody DepositRequest request) {
        accountService.withdraw(accountNumber, request.getAmount());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{accountNumber}/deposit")
    public ResponseEntity<Void> deposit(@PathVariable String accountNumber, @Valid @RequestBody DepositRequest request) {
        accountService.deposit(accountNumber, request.getAmount());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{fromAccountNumber}/transfer/{toAccountNumber}")
    public ResponseEntity<Void> transfer(@PathVariable String fromAccountNumber, @PathVariable String toAccountNumber,
            @Valid @RequestBody DepositRequest request) {
        accountService.transfer(fromAccountNumber, toAccountNumber, request.getAmount());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{accountNumber}/transactions")
    public ResponseEntity<Page<Transaction>> getTransactions(
            @PathVariable String accountNumber,
            @PageableDefault(size = 20, sort = "timestamp", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(accountService.getTransactions(accountNumber, pageable));
    }

    @PatchMapping("/{accountNumber}")
    public ResponseEntity<Account> updateAccount(@PathVariable String accountNumber,
            @Valid @RequestBody UpdateAccountRequest request) {
        Account account = accountService.updateAccount(accountNumber, request);
        return ResponseEntity.ok(account);
    }

    @DeleteMapping("/{accountNumber}/close")
    public ResponseEntity<Void> closeAccount(@PathVariable String accountNumber) {
        accountService.closeAccount(accountNumber);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{accountNumber}/activate")
    public ResponseEntity<Void> activateAccount(@PathVariable String accountNumber) {
        accountService.activateAccount(accountNumber);
        return ResponseEntity.ok().build();
    }
}