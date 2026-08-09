package com.banking.bankingsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banking.bankingsystem.dto.CreateBeneficiaryRequest;
import com.banking.bankingsystem.model.Account;
import com.banking.bankingsystem.model.Beneficiary;
import com.banking.bankingsystem.repository.AccountRepository;
import com.banking.bankingsystem.repository.BeneficiaryRepository;

@Service
public class BeneficiaryService {
    private final AccountRepository accountRepository;
    private final BeneficiaryRepository beneficiaryRepository;

    @Autowired
    public BeneficiaryService(BeneficiaryRepository beneficiaryRepository, AccountRepository accountRepository) {
        this.beneficiaryRepository = beneficiaryRepository;
        this.accountRepository = accountRepository;
    }

    public Beneficiary getBeneficiary(Long id){
        return beneficiaryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Beneficiary not found"));
    }

    public Beneficiary createBeneficiary(Long accountId, CreateBeneficiaryRequest request) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account Not Found"));

        Boolean exists = accountRepository.existsByAccountNumber(request.getBeneficiaryAccountNumber());
        if (!exists) {
            throw new RuntimeException("No account found with that account number");
        }

        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setNickname(request.getNickname());
        beneficiary.setBeneficiaryAccountNumber(request.getBeneficiaryAccountNumber());
        beneficiary.setAccount(account);
        return beneficiaryRepository.save(beneficiary);
    }
}