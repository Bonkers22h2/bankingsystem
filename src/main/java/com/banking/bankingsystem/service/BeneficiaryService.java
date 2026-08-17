package com.banking.bankingsystem.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.banking.bankingsystem.dto.CreateBeneficiaryRequest;
import com.banking.bankingsystem.dto.UpdateBeneficiaryRequest;
import com.banking.bankingsystem.exception.AccountNotFoundException;
import com.banking.bankingsystem.exception.BeneficiaryNotFoundException;
import com.banking.bankingsystem.exception.ClosedAccountException;
import com.banking.bankingsystem.model.Account;
import com.banking.bankingsystem.model.Beneficiary;
import com.banking.bankingsystem.repository.AccountRepository;
import com.banking.bankingsystem.repository.BeneficiaryRepository;

@Service
public class BeneficiaryService {

    private final AccountRepository accountRepository;
    private final BeneficiaryRepository beneficiaryRepository;

    public BeneficiaryService(BeneficiaryRepository beneficiaryRepository, AccountRepository accountRepository) {
        this.beneficiaryRepository = beneficiaryRepository;
        this.accountRepository = accountRepository;
    }

    public Beneficiary getBeneficiary(String accountNumber, Long id) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new AccountNotFoundException("No account found"));

        Beneficiary beneficiary = beneficiaryRepository.findById(id)
            .orElseThrow(() -> new BeneficiaryNotFoundException("No beneficiary found"));

        if (!account.isActive()) {
            throw new ClosedAccountException("This account is close");
        }

        if (!beneficiary.getAccount().getId().equals(account.getId())) {
            throw new BeneficiaryNotFoundException("This beneficiary does not belong to the specified account");
        }

        return beneficiary;
    }

    public List<Beneficiary> getAllBeneficiary(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new AccountNotFoundException("Account Not Found!"));

        return beneficiaryRepository.findByAccountId(account.getId());
    }

    public Beneficiary createBeneficiary(String accountNumber, CreateBeneficiaryRequest request) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setNickname(request.getNickname());
        beneficiary.setBeneficiaryAccountNumber(request.getBeneficiaryAccountNumber());
        beneficiary.setAccount(account);
        return beneficiaryRepository.save(beneficiary);
    }

    public Beneficiary updateBeneficiary(String accountNumber, Long beneficiaryId, UpdateBeneficiaryRequest request) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        Beneficiary beneficiary = beneficiaryRepository.findById(beneficiaryId)
                .orElseThrow(() -> new BeneficiaryNotFoundException("Beneficiary not found"));

        if (!beneficiary.getAccount().getId().equals(account.getId())) {
            throw new BeneficiaryNotFoundException("This beneficiary does not belong to the specified account");
        }

        beneficiary.setNickname(request.getNickname());
        beneficiary.setBeneficiaryAccountNumber(request.getBeneficiaryAccountNumber());
        return beneficiaryRepository.save(beneficiary);
    }

    public void deleteBeneficiary(String accountNumber, Long beneficiaryId) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        Beneficiary beneficiary = beneficiaryRepository.findById(beneficiaryId)
            .orElseThrow(() -> new BeneficiaryNotFoundException("Beneficiary not found"));

        if (!beneficiary.getAccount().getId().equals(account.getId())) {
            throw new BeneficiaryNotFoundException("This beneficiary does not belong to the specified account");
        }

        beneficiaryRepository.delete(beneficiary);
    }
}