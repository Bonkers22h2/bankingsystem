package com.banking.bankingsystem.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateBeneficiaryRequest {

    @NotBlank
    private String nickname;

    @NotBlank
    private String beneficiaryAccountNumber;

    public String getBeneficiaryAccountNumber() {
        return this.beneficiaryAccountNumber;
    }

    public void setBeneficiaryAccountNumber(String beneficiaryAccountNumber) {
        this.beneficiaryAccountNumber = beneficiaryAccountNumber;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

}