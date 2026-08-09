package com.banking.bankingsystem.repository;

import com.banking.bankingsystem.model.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {
}