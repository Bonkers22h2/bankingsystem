package com.banking.bankingsystem.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.PositiveOrZero;

public class CreateAccountRequest {

    @NotBlank
    private String ownerName;
    @NotBlank
    private String accountType;
    @PositiveOrZero
    private BigDecimal initialBalance; 


    public String getOwnerName() {
        return this.ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getAccountType() {
        return this.accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getInitialBalance() {
        return this.initialBalance;
    }

    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }

}