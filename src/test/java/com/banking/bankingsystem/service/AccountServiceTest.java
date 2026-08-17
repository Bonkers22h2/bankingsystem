package com.banking.bankingsystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.banking.bankingsystem.exception.AccountNotFoundException;
import com.banking.bankingsystem.exception.ClosedAccountException;
import com.banking.bankingsystem.exception.InsufficientFundsException;
import com.banking.bankingsystem.exception.InvalidAmountException;
import com.banking.bankingsystem.model.Account;
import com.banking.bankingsystem.repository.AccountRepository;
import com.banking.bankingsystem.repository.TransactionRepository;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    void withdraw_shouldThrowException_whenBalanceIsInsufficient() {
        Account account = new Account();
        account.setAccountNumber("1111111111");
        account.setBalance(new BigDecimal("100"));

        when(accountRepository.findByAccountNumber("1111111111")).thenReturn(Optional.of(account));

        assertThrows(InsufficientFundsException.class, () -> {
            accountService.withdraw("1111111111", new BigDecimal("500"));
        });
    }

    @Test
    void withdraw_shouldThrowException_whenAmountBelowMinimum() {
        Account account = new Account();
        account.setAccountNumber("1111111111");
        account.setBalance(new BigDecimal("1000"));

        when(accountRepository.findByAccountNumber("1111111111")).thenReturn(Optional.of(account));

        assertThrows(InvalidAmountException.class, () -> {
            accountService.withdraw("1111111111", new BigDecimal("10"));
        });
    }

    @Test
    void withdraw_shouldThrowException_whenAmountAboveMaximum() {
        Account account = new Account();
        account.setAccountNumber("1111111111");
        account.setBalance(new BigDecimal("1000"));

        when(accountRepository.findByAccountNumber("1111111111")).thenReturn(Optional.of(account));

        assertThrows(InvalidAmountException.class, () -> {
            accountService.withdraw("1111111111", new BigDecimal("100000000"));
        });
    }

    @Test
    void withdraw_shouldReduceBalance_whenAmountIsValid() {
        Account account = new Account();
        account.setAccountNumber("1111111111");
        account.setBalance(new BigDecimal("1000"));

        when(accountRepository.findByAccountNumber("1111111111")).thenReturn(Optional.of(account));

        accountService.withdraw("1111111111", new BigDecimal("300"));

        assertEquals(new BigDecimal("700"), account.getBalance());
    }

    @Test
    void transferMoney_increaseBalanceBeneficiary_decreaseBalanceSource() {
        Account sourceAccount = new Account();
        sourceAccount.setAccountNumber("1111111111");
        sourceAccount.setBalance(new BigDecimal("1000"));

        Account destinationAccount = new Account();
        destinationAccount.setAccountNumber("2222222222");
        destinationAccount.setBalance(new BigDecimal("100"));

        when(accountRepository.findByAccountNumber("1111111111")).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountNumber("2222222222")).thenReturn(Optional.of(destinationAccount));

        accountService.transfer("1111111111", "2222222222", new BigDecimal("300"));

        assertEquals(new BigDecimal("700"), sourceAccount.getBalance());
        assertEquals(new BigDecimal("400"), destinationAccount.getBalance());
    }

    @Test
    void transferMoney_shouldThrowException_whenSourceHasInsufficientFunds() {
        Account sourceAccount = new Account();
        sourceAccount.setAccountNumber("1111111111");
        sourceAccount.setBalance(new BigDecimal("200"));

        Account destinationAccount = new Account();
        destinationAccount.setAccountNumber("2222222222");
        destinationAccount.setBalance(new BigDecimal("150"));

        when(accountRepository.findByAccountNumber("1111111111")).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountNumber("2222222222")).thenReturn(Optional.of(destinationAccount));

        assertThrows(InsufficientFundsException.class, () -> {
            accountService.transfer("1111111111", "2222222222", new BigDecimal("300"));
        });
    }

    @Test
    void transferMoney_shouldThrowException_whenSourceAccountIsClosed() {
        Account sourceAccount = new Account();
        sourceAccount.setAccountNumber("1111111111");
        sourceAccount.setBalance(new BigDecimal("200"));
        sourceAccount.setActive(false);

        Account destinationAccount = new Account();
        destinationAccount.setAccountNumber("2222222222");
        destinationAccount.setBalance(new BigDecimal("150"));

        when(accountRepository.findByAccountNumber("1111111111")).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountNumber("2222222222")).thenReturn(Optional.of(destinationAccount));

        assertThrows(ClosedAccountException.class, () -> {
            accountService.transfer("1111111111", "2222222222", new BigDecimal("100"));
        });
    }

    @Test
    void deposit_shouldThrowException_whenAccountIsClosed() {
        Account account = new Account();
        account.setAccountNumber("1111111111");
        account.setBalance(new BigDecimal("1000"));
        account.setActive(false);

        when(accountRepository.findByAccountNumber("1111111111")).thenReturn(Optional.of(account));

        assertThrows(ClosedAccountException.class, () -> {
            accountService.deposit("1111111111", new BigDecimal("100"));
        });
    }

    @Test
    void deposit_shouldThrowException_whenAmountIsBelowMinimum() {
        Account account = new Account();
        account.setAccountNumber("1111111111");
        account.setBalance(new BigDecimal("1000"));

        when(accountRepository.findByAccountNumber("1111111111")).thenReturn(Optional.of(account));

        assertThrows(InvalidAmountException.class, () -> {
            accountService.deposit("1111111111", new BigDecimal("10"));
        });
    }

    @Test
    void deposit_shouldThrowException_whenAmountIsAboveMaximum() {
        Account account = new Account();
        account.setAccountNumber("1111111111");
        account.setBalance(new BigDecimal("1000"));

        when(accountRepository.findByAccountNumber("1111111111")).thenReturn(Optional.of(account));

        assertThrows(InvalidAmountException.class, () -> {
            accountService.deposit("1111111111", new BigDecimal("1000000"));
        });
    }

    @Test
    void deposit_shouldThrowException_whenInsufficientBalance() {
        Account account = new Account();
        account.setAccountNumber("1111111111");
        account.setBalance(new BigDecimal("100"));

        when(accountRepository.findByAccountNumber("1111111111")).thenReturn(Optional.of(account));

        assertThrows(InsufficientFundsException.class, () -> {
            accountService.deposit("1111111111", new BigDecimal("1000"));
        });
    }

    @Test
    void deposit_shouldIncreaseBalance_whenAmountIsValid() {
        Account account = new Account();
        account.setAccountNumber("1111111111");
        account.setBalance(new BigDecimal("1000"));

        when(accountRepository.findByAccountNumber("1111111111")).thenReturn(Optional.of(account));

        accountService.deposit("1111111111", new BigDecimal("300"));

        assertEquals(new BigDecimal("1300"), account.getBalance());
    }

    @Test
    void withdraw_shouldThrowException_whenAccountIsClosed() {
        Account account = new Account();
        account.setAccountNumber("1111111111");
        account.setBalance(new BigDecimal("1000"));
        account.setActive(false);

        when(accountRepository.findByAccountNumber("1111111111")).thenReturn(Optional.of(account));

        assertThrows(ClosedAccountException.class, () -> {
            accountService.withdraw("1111111111", new BigDecimal("300"));
        });
    }

    @Test
    void transfer_shouldThrowException_whenDestinationAccountIsClosed() {
        Account sourceAccount = new Account();
        sourceAccount.setAccountNumber("1111111111");
        sourceAccount.setBalance(new BigDecimal("1000"));

        Account destinationAccount = new Account();
        destinationAccount.setAccountNumber("2222222222");
        destinationAccount.setBalance(new BigDecimal("500"));
        destinationAccount.setActive(false);

        when(accountRepository.findByAccountNumber("1111111111")).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountNumber("2222222222")).thenReturn(Optional.of(destinationAccount));

        assertThrows(ClosedAccountException.class, () -> {
            accountService.transfer("1111111111", "2222222222", new BigDecimal("300"));
        });
    }

    @Test
    void transfer_shouldThrowException_whenAmountBelowMinimum() {
        Account sourceAccount = new Account();
        sourceAccount.setAccountNumber("1111111111");
        sourceAccount.setBalance(new BigDecimal("1000"));

        Account destinationAccount = new Account();
        destinationAccount.setAccountNumber("2222222222");
        destinationAccount.setBalance(new BigDecimal("500"));

        when(accountRepository.findByAccountNumber("1111111111")).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountNumber("2222222222")).thenReturn(Optional.of(destinationAccount));

        assertThrows(InvalidAmountException.class, () -> {
            accountService.transfer("1111111111", "2222222222", new BigDecimal("10"));
        });
    }

    @Test
    void transfer_shouldThrowException_whenAmountAboveMaximum() {
        Account sourceAccount = new Account();
        sourceAccount.setAccountNumber("1111111111");
        sourceAccount.setBalance(new BigDecimal("1000"));

        Account destinationAccount = new Account();
        destinationAccount.setAccountNumber("2222222222");
        destinationAccount.setBalance(new BigDecimal("500"));

        when(accountRepository.findByAccountNumber("1111111111")).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountNumber("2222222222")).thenReturn(Optional.of(destinationAccount));

        assertThrows(InvalidAmountException.class, () -> {
            accountService.transfer("1111111111", "2222222222", new BigDecimal("999999999"));
        });
    }

    @Test
    void withdraw_shouldThrowException_whenAccountNotFound() {
        when(accountRepository.findByAccountNumber("1111111111")).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> {
            accountService.withdraw("1111111111", new BigDecimal("300"));
        });
    }
}