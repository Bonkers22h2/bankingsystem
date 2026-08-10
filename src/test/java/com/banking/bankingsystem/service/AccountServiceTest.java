package com.banking.bankingsystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.lang.StackWalker.Option;
import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        account.setId(1L);
        account.setBalance(new BigDecimal("100"));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        assertThrows(InsufficientFundsException.class, () -> {
            accountService.withdraw(1L, new BigDecimal("500"));
        });
    }

    @Test
    void withdraw_shouldThrowException_whenAmountBelowMinimum() {
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal("1000"));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        assertThrows(InvalidAmountException.class, () -> {
            accountService.withdraw(1L, new BigDecimal("10"));
        });
    }

    @Test
    void withdraw_shouldThrowException_whenAmountAboveMaximum() {
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal("1000"));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        assertThrows(InvalidAmountException.class, () -> {
            accountService.withdraw(1L, new BigDecimal("100000000"));
        });
    }

    @Test
    void withdraw_shouldReduceBalance_whenAmountIsValid() {
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal("1000"));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        accountService.withdraw(1L, new BigDecimal("300"));

        assertEquals(new BigDecimal("700"), account.getBalance());
    }

    @Test
    void transferMoney_increaseBalanceBeneficiary_decreaseBalanceSource() {
        Account sourceAccount = new Account();
        sourceAccount.setId(1L);
        sourceAccount.setBalance(new BigDecimal("1000"));

        Account destinationAccount = new Account();
        destinationAccount.setId(2L);
        destinationAccount.setBalance(new BigDecimal("100"));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(destinationAccount));

        accountService.transfer(1L, 2L, new BigDecimal("300"));

        assertEquals(new BigDecimal("700"), sourceAccount.getBalance());
        assertEquals(new BigDecimal("400"), destinationAccount.getBalance());
    }

    @Test
    void trasnferMoney_shouldThrowException_whenSourceHasInsufficientFounds() {
        Account sourceAccount = new Account();
        sourceAccount.setId(1L);
        sourceAccount.setBalance(new BigDecimal("200"));

        Account destinationAccount = new Account();
        destinationAccount.setId(2L);
        destinationAccount.setBalance(new BigDecimal("150"));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(destinationAccount));

        assertThrows(InsufficientFundsException.class, () -> {
            accountService.transfer(1L, 2L, new BigDecimal("300"));
        });
    }

    @Test
    void trasnferMoney_shouldThrowException_whenSrouceAccountIsClosed() {
        Account sourceAccount = new Account();
        sourceAccount.setId(1L);
        sourceAccount.setBalance(new BigDecimal("200"));
        sourceAccount.setActive(false);

        Account destinationAccount = new Account();
        destinationAccount.setId(2L);
        destinationAccount.setBalance(new BigDecimal("150"));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(destinationAccount));

        assertThrows(ClosedAccountException.class, () -> {
            accountService.transfer(1L, 2L, new BigDecimal("100"));
        });
    }
}