package com.banking.bankingsystem.exception;

public class ClosedAccountException extends RuntimeException {
    public ClosedAccountException(String message) {
        super(message);
    }
}