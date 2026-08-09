package com.banking.bankingsystem.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.PositiveOrZero;

public class DepositRequest {

    @PositiveOrZero
    private BigDecimal amount;

    public DepositRequest() {
        // TODO Auto-generated method stub
    }


    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}