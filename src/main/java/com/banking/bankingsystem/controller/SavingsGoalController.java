package com.banking.bankingsystem.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.bankingsystem.dto.ContributeSavingsGoalRequest;
import com.banking.bankingsystem.dto.CreateSavingsGoalRequest;
import com.banking.bankingsystem.model.SavingsGoal;
import com.banking.bankingsystem.service.SavingsGoalService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

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

    @PostMapping("/{id}")
    public ResponseEntity<SavingsGoal> contributeSavingsGoal(
            @PathVariable Long accountId,
            @PathVariable Long id,
            @Valid @RequestBody ContributeSavingsGoalRequest request) {
        SavingsGoal savingsGoal = savingsGoalService.contribute(accountId, id, request);
        return ResponseEntity.ok(savingsGoal);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SavingsGoal> getSavingsGoal(@PathVariable Long id) {
        SavingsGoal savingsGoal = savingsGoalService.getSavingsGoal(id);
        return ResponseEntity.ok(savingsGoal);
    }
    
}