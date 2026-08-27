package com.banking.bankingsystem.repository;

import com.banking.bankingsystem.model.Account;
import com.banking.bankingsystem.model.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByAccountNumber(String accountNumber);

    Optional<Account> findByAccountNumber(String accountNumber);

    Optional<Account> findById(Long id);

    List<Account> findByOwner(User owner);
}