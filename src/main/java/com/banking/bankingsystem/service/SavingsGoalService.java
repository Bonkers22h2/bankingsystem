package com.banking.bankingsystem.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.banking.bankingsystem.dto.CreateSavingsGoalRequest;
import com.banking.bankingsystem.model.Account;
import com.banking.bankingsystem.model.SavingsGoal;
import com.banking.bankingsystem.repository.AccountRepository;
import com.banking.bankingsystem.repository.SavingsGoalRepository;

@Service
public class SavingsGoalService {
    private final SavingsGoalRepository savingsGoalRepository;
    private final AccountRepository accountRepository;

    public SavingsGoalService(SavingsGoalRepository savingsGoalRepository, AccountRepository accountRepository){
        this.savingsGoalRepository = savingsGoalRepository;
        this.accountRepository = accountRepository;
    }

    public SavingsGoal createSavingsGoal(Long accountId, CreateSavingsGoalRequest request) {
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new RuntimeException("Account not found!"));

        SavingsGoal savingsGoal = new SavingsGoal();
        savingsGoal.setGoalName(request.getGoalName());
        savingsGoal.setTargetAmount(request.getTargetAmount());
        savingsGoal.setAccount(account);
        savingsGoal.setCurrentAmount(new BigDecimal(0));
        return savingsGoalRepository.save(savingsGoal);
    }
}