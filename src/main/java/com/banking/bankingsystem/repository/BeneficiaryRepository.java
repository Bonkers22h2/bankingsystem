package com.banking.bankingsystem.repository;

import com.banking.bankingsystem.model.Beneficiary;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {
    List<Beneficiary> findByAccountId(Long accountId);
}