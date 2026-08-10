package com.banking.bankingsystem.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateBeneficiaryRequest {
    
    @NotBlank
    private String nickname;

    @NotBlank
    private String beneficiaryAccountNumber;


    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBeneficiaryAccountNumber() {
        return this.beneficiaryAccountNumber;
    }

    public void setBeneficiaryAccountNumber(String beneficiaryAccountNumber) {
        this.beneficiaryAccountNumber = beneficiaryAccountNumber;
    }

}