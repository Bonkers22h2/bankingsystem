package com.banking.bankingsystem.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.PositiveOrZero;

public class ContributeSavingsGoalRequest {
    @PositiveOrZero
    private BigDecimal amount;


    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}