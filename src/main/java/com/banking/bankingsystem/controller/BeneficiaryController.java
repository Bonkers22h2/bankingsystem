package com.banking.bankingsystem.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.bankingsystem.dto.CreateBeneficiaryRequest;
import com.banking.bankingsystem.model.Beneficiary;
import com.banking.bankingsystem.service.BeneficiaryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/accounts/{accountId}/beneficiaries")
public class BeneficiaryController {

    private final BeneficiaryService beneficiaryService;

    @Autowired
    public BeneficiaryController(BeneficiaryService beneficiaryService){
        this.beneficiaryService = beneficiaryService;
    }

    @PostMapping
    public ResponseEntity<Beneficiary> createBeneficiary(@PathVariable Long accountId, @Valid @RequestBody CreateBeneficiaryRequest request) {
        Beneficiary beneficiary = beneficiaryService.createBeneficiary(accountId, request);
        return ResponseEntity.ok(beneficiary);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Beneficiary> getBeneficiary(@PathVariable Long id){
        Beneficiary beneficiary = beneficiaryService.getBeneficiary(id);
        return ResponseEntity.ok(beneficiary);
    }
    
}