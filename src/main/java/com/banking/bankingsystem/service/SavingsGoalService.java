package com.banking.bankingsystem.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.banking.bankingsystem.dto.ContributeSavingsGoalRequest;
import com.banking.bankingsystem.dto.CreateSavingsGoalRequest;
import com.banking.bankingsystem.exception.AccountNotFoundException;
import com.banking.bankingsystem.exception.ClosedAccountException;
import com.banking.bankingsystem.exception.GoalLimitExceedException;
import com.banking.bankingsystem.exception.InsufficientFundsException;
import com.banking.bankingsystem.exception.SavingsGoalNotFoundException;
import com.banking.bankingsystem.model.Account;
import com.banking.bankingsystem.model.SavingsGoal;
import com.banking.bankingsystem.repository.AccountRepository;
import com.banking.bankingsystem.repository.SavingsGoalRepository;

import jakarta.transaction.Transactional;

@Service
public class SavingsGoalService {

    private final SavingsGoalRepository savingsGoalRepository;
    private final AccountRepository accountRepository;

    public SavingsGoalService(SavingsGoalRepository savingsGoalRepository, AccountRepository accountRepository) {
        this.savingsGoalRepository = savingsGoalRepository;
        this.accountRepository = accountRepository;
    }

    public SavingsGoal createSavingsGoal(String accountNumber, CreateSavingsGoalRequest request) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found!"));

        SavingsGoal savingsGoal = new SavingsGoal();
        savingsGoal.setGoalName(request.getGoalName());
        savingsGoal.setTargetAmount(request.getTargetAmount());
        savingsGoal.setAccount(account);
        savingsGoal.setCurrentAmount(new BigDecimal(0));
        return savingsGoalRepository.save(savingsGoal);
    }

    @Transactional
    public SavingsGoal contribute(String accountNumber, Long id, ContributeSavingsGoalRequest request) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found!"));

        SavingsGoal savingsGoal = savingsGoalRepository.findById(id)
                .orElseThrow(() -> new SavingsGoalNotFoundException("Savings Goal not found!"));

        if (!account.isActive()) {
            throw new ClosedAccountException("This account is close");
        }

        if (!savingsGoal.getAccount().getId().equals(account.getId())) {
            throw new SavingsGoalNotFoundException("This goal does not belong to the specified account");
        }

        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds in account: " + accountNumber);
        }

        BigDecimal newCurrentAmount = savingsGoal.getCurrentAmount().add(request.getAmount());

        if (newCurrentAmount.compareTo(savingsGoal.getTargetAmount()) > 0) {
            throw new GoalLimitExceedException("Contribution would exceed the goal's target amount");
        }

        account.setBalance(account.getBalance().subtract(request.getAmount()));
        savingsGoal.setCurrentAmount(newCurrentAmount);

        accountRepository.save(account);
        return savingsGoalRepository.save(savingsGoal);
    }

    public SavingsGoal getSavingsGoal(String accountNumber, Long id) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("No Account found"));

        SavingsGoal savingsGoal = savingsGoalRepository.findById(id)
                .orElseThrow(() -> new SavingsGoalNotFoundException("Savings Goal not found"));

        if (!account.isActive()) {
            throw new ClosedAccountException("This account is close");
        }

        if (!savingsGoal.getAccount().getId().equals(account.getId())) {
            throw new SavingsGoalNotFoundException("This Savings goal does not belong to the specified account");
        }

        return savingsGoal;
    }

    public void deleteSavingsGoal(String accountNumber, Long id) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("No Account found"));

        SavingsGoal savingsGoal = savingsGoalRepository.findById(id)
                .orElseThrow(() -> new SavingsGoalNotFoundException("Savings Goal not found"));

        if (!savingsGoal.getAccount().getId().equals(account.getId())) {
            throw new SavingsGoalNotFoundException("This Savings Goal does not belong to the specified account");
        }

        savingsGoalRepository.delete(savingsGoal);
    }
}