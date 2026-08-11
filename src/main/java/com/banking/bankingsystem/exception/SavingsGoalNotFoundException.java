package com.banking.bankingsystem.exception;

public class SavingsGoalNotFoundException extends RuntimeException {
    public SavingsGoalNotFoundException (String message) {
        super(message);
    }
}