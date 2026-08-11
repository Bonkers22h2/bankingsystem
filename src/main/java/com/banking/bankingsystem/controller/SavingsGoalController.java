package com.banking.bankingsystem.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.bankingsystem.dto.CreateSavingsGoalRequest;
import com.banking.bankingsystem.model.SavingsGoal;
import com.banking.bankingsystem.service.SavingsGoalService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/accounts/{accountId}/savingsGoal")
public class SavingsGoalController {

    private final SavingsGoalService savingsGoalService;

    public SavingsGoalController(SavingsGoalService savingsGoalService) {
        this.savingsGoalService = savingsGoalService;
    }

    @PostMapping
    public ResponseEntity<SavingsGoal> createSavingsGoal(
            @PathVariable Long accountId,
            @Valid @RequestBody CreateSavingsGoalRequest request) {
        SavingsGoal savingsGoal = savingsGoalService.createSavingsGoal(accountId, request);
        return ResponseEntity.ok(savingsGoal);
    }

}