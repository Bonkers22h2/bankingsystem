package com.banking.bankingsystem.exception;

public class GoalLimitExceedException extends RuntimeException {
    public GoalLimitExceedException(String message) {
        super(message);
    }
}