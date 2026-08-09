package com.banking.bankingsystem.service;

import com.banking.bankingsystem.dto.CreateAccountRequest;
import com.banking.bankingsystem.exception.InsufficientFundsException;
import com.banking.bankingsystem.model.*;
import com.banking.bankingsystem.repository.AccountRepository;
import com.banking.bankingsystem.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public void deposit(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        logTransaction(TransactionType.DEPOSIT, amount, account, null);
    }

    @Transactional
    public void withdraw(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds in account " + accountId);
        }

        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);

        logTransaction(TransactionType.WITHDRAWAL, amount, account, null);
    }

    @Transactional
    public void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new RuntimeException("Source account not found"));
        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new RuntimeException("Destination account not found"));

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds in account " + fromAccountId);
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        logTransaction(TransactionType.TRANSFER, amount, fromAccount, toAccount);
    }

    private void logTransaction(TransactionType type, BigDecimal amount, Account account, Account relatedAccount) {
        Transaction transaction = new Transaction();
        transaction.setType(type);
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setAccount(account);
        transaction.setRelatedAccount(relatedAccount);
        transactionRepository.save(transaction);
    }

    public Account createAccount(CreateAccountRequest request) {
        Account account = new Account();
        account.setOwnerName(request.getOwnerName());
        account.setAccountType(AccountType.valueOf(request.getAccountType()));
        account.setBalance(request.getInitialBalance());
        account.setCreatedAt(LocalDateTime.now());
        account.setAccountNumber(generateUniqueAccountNumber()); // NEW
        return accountRepository.save(account);
    }

    public Account getAccount(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    private String generateUniqueAccountNumber() {
        String accountNumber;
        do {
            accountNumber = generateAccountNumber();
        } while (accountRepository.existsByAccountNumber(accountNumber));
        return accountNumber;
    }

    private String generateAccountNumber() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(random.nextInt(10)); // random digit 0-9
        }
        return sb.toString();
    }
}