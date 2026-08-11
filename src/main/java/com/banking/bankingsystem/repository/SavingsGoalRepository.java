package com.banking.bankingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banking.bankingsystem.model.SavingsGoal;

public interface SavingsGoalRepository extends JpaRepository<SavingsGoal, Long>  {
    
}