package com.banking.bankingsystem.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateAccountRequest {
    @NotBlank
    private String ownerName;


    public String getOwnerName() {
        return this.ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

}