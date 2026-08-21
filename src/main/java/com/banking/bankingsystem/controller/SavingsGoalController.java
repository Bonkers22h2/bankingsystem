package com.banking.bankingsystem.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.banking.bankingsystem.dto.ContributeSavingsGoalRequest;
import com.banking.bankingsystem.dto.CreateSavingsGoalRequest;
import com.banking.bankingsystem.model.SavingsGoal;
import com.banking.bankingsystem.service.SavingsGoalService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

@CrossOrigin(origins = "http://localhost:4200", methods = {
        RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH, RequestMethod.DELETE
})
@RestController
@RequestMapping("/accounts/{accountNumber}/savingsGoal")
public class SavingsGoalController {

    private final SavingsGoalService savingsGoalService;

    public SavingsGoalController(SavingsGoalService savingsGoalService) {
        this.savingsGoalService = savingsGoalService;
    }

    @GetMapping
    public ResponseEntity<List<SavingsGoal>> getAllSavingsGoal(@PathVariable String accountNumber){
        List<SavingsGoal> savingsGoal = savingsGoalService.getAllSavingsGoal(accountNumber);
        return ResponseEntity.ok(savingsGoal);
    }

    @PostMapping
    public ResponseEntity<SavingsGoal> createSavingsGoal(
            @PathVariable String accountNumber,
            @Valid @RequestBody CreateSavingsGoalRequest request) {
        SavingsGoal savingsGoal = savingsGoalService.createSavingsGoal(accountNumber, request);
        return ResponseEntity.ok(savingsGoal);
    }

    @PostMapping("/{id}")
    public ResponseEntity<SavingsGoal> contributeSavingsGoal(
            @PathVariable String accountNumber,
            @PathVariable Long id,
            @Valid @RequestBody ContributeSavingsGoalRequest request) {
        SavingsGoal savingsGoal = savingsGoalService.contribute(accountNumber, id, request);
        return ResponseEntity.ok(savingsGoal);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SavingsGoal> getSavingsGoal(@PathVariable String accountNumber, @PathVariable Long id) {
        SavingsGoal savingsGoal = savingsGoalService.getSavingsGoal(accountNumber, id);
        return ResponseEntity.ok(savingsGoal);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SavingsGoal> deleteSavingsGoal(
            @PathVariable String accountNumber,
            @PathVariable Long id) {
        savingsGoalService.deleteSavingsGoal(accountNumber, id);
        return ResponseEntity.ok().build();
    }
}