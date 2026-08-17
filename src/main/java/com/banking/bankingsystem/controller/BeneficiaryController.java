package com.banking.bankingsystem.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.banking.bankingsystem.dto.CreateBeneficiaryRequest;
import com.banking.bankingsystem.dto.UpdateBeneficiaryRequest;
import com.banking.bankingsystem.model.Beneficiary;
import com.banking.bankingsystem.service.BeneficiaryService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:4200", methods = {
        RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH, RequestMethod.DELETE
})
@RestController
@RequestMapping("/accounts/{accountNumber}/beneficiaries")
public class BeneficiaryController {

    private final BeneficiaryService beneficiaryService;

    public BeneficiaryController(BeneficiaryService beneficiaryService) {
        this.beneficiaryService = beneficiaryService;
    }

    @GetMapping
    public ResponseEntity<List<Beneficiary>> getAllBeneficiary(@PathVariable String accountNumber) {
        List<Beneficiary> beneficiaries = beneficiaryService.getAllBeneficiary(accountNumber);
        return ResponseEntity.ok(beneficiaries);
    }

    @PostMapping
    public ResponseEntity<Beneficiary> createBeneficiary(
            @PathVariable String accountNumber,
            @Valid @RequestBody CreateBeneficiaryRequest request) {
        Beneficiary beneficiary = beneficiaryService.createBeneficiary(accountNumber, request);
        return ResponseEntity.ok(beneficiary);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Beneficiary> getBeneficiary(@PathVariable String accountNumber, @PathVariable Long id) {
        Beneficiary beneficiary = beneficiaryService.getBeneficiary(accountNumber, id);
        return ResponseEntity.ok(beneficiary);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Beneficiary> updateBeneficiary(
            @PathVariable String accountNumber,
            @PathVariable Long id,
            @Valid @RequestBody UpdateBeneficiaryRequest request) {
        Beneficiary beneficiary = beneficiaryService.updateBeneficiary(accountNumber, id, request);
        return ResponseEntity.ok(beneficiary);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Beneficiary> deleteBeneficiary(
            @PathVariable String accountNumber,
            @PathVariable Long id) {
        beneficiaryService.deleteBeneficiary(accountNumber, id);
        return ResponseEntity.ok().build();
    }
}