package com.banking.bankingsystem.service;

import com.banking.bankingsystem.controller.AccountController;
import org.springframework.stereotype.Service;

import com.banking.bankingsystem.dto.CreateBeneficiaryRequest;
import com.banking.bankingsystem.dto.UpdateBeneficiaryRequest;
import com.banking.bankingsystem.exception.ClosedAccountException;
import com.banking.bankingsystem.model.Account;
import com.banking.bankingsystem.model.Beneficiary;
import com.banking.bankingsystem.repository.AccountRepository;
import com.banking.bankingsystem.repository.BeneficiaryRepository;

@Service
public class BeneficiaryService {

    private final AccountController accountController;
    private final AccountRepository accountRepository;
    private final BeneficiaryRepository beneficiaryRepository;

    public BeneficiaryService(BeneficiaryRepository beneficiaryRepository, AccountRepository accountRepository, AccountController accountController) {
        this.beneficiaryRepository = beneficiaryRepository;
        this.accountRepository = accountRepository;
        this.accountController = accountController;
    }

    public Beneficiary getBeneficiary(Long accountId, Long id) {
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new RuntimeException("No account found"));

        Beneficiary beneficiary = beneficiaryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("No beneficiary found"));

        if(!account.isActive()){
            throw new ClosedAccountException("This account is close");
        }

        if(!beneficiary.getAccount().getId().equals(accountId)){
            throw new RuntimeException("This beneficiary does not belong to the specified account");
        }

        return beneficiaryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Beneficiary not found"));
    }

    public Beneficiary createBeneficiary(Long accountId, CreateBeneficiaryRequest request) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setNickname(request.getNickname());
        beneficiary.setBeneficiaryAccountNumber(request.getBeneficiaryAccountNumber());
        beneficiary.setAccount(account);
        return beneficiaryRepository.save(beneficiary);
    }

    public Beneficiary updateBeneficiary(Long accountId, Long beneficiaryId, UpdateBeneficiaryRequest request) {
        Beneficiary beneficiary = beneficiaryRepository.findById(beneficiaryId)
                .orElseThrow(() -> new RuntimeException("Beneficiary not found"));

        if (!beneficiary.getAccount().getId().equals(accountId)) {
            throw new RuntimeException("This beneficiary does not belong to the specified account");
        }

        beneficiary.setNickname(request.getNickname());
        beneficiary.setBeneficiaryAccountNumber(request.getBeneficiaryAccountNumber());
        return beneficiaryRepository.save(beneficiary);
    }

    public void deleteBeneficiary(Long accountId, Long beneficiaryId) {
        Beneficiary beneficiary = beneficiaryRepository.findById(beneficiaryId)
            .orElseThrow(() -> new RuntimeException( "Beneficiary not found"));

        if (!beneficiary.getAccount().getId().equals(accountId)) {
            throw new RuntimeException("This beneficiary does not belong to the specified account");
        }

        beneficiaryRepository.delete(beneficiary);
    }
}