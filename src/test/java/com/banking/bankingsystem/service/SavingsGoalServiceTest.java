package com.banking.bankingsystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.banking.bankingsystem.dto.ContributeSavingsGoalRequest;
import com.banking.bankingsystem.dto.CreateSavingsGoalRequest;
import com.banking.bankingsystem.exception.ClosedAccountException;
import com.banking.bankingsystem.exception.GoalLimitExceedException;
import com.banking.bankingsystem.exception.InsufficientFundsException;
import com.banking.bankingsystem.exception.SavingsGoalNotFoundException;
import com.banking.bankingsystem.model.Account;
import com.banking.bankingsystem.model.SavingsGoal;
import com.banking.bankingsystem.repository.AccountRepository;
import com.banking.bankingsystem.repository.SavingsGoalRepository;

@ExtendWith(MockitoExtension.class)
class SavingsGoalServiceTest {

    @Mock
    private SavingsGoalRepository savingsGoalRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private SavingsGoalService savingsGoalService;

    @Test
    void createSavingsGoal_shouldSaveWithZeroCurrentAmount() {
        Account account = new Account();
        account.setId(1L);

        CreateSavingsGoalRequest request = new CreateSavingsGoalRequest();
        request.setGoalName("RTX 5090");
        request.setTargetAmount(new BigDecimal("50000"));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(savingsGoalRepository.save(any(SavingsGoal.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        SavingsGoal result = savingsGoalService.createSavingsGoal(1L, request);

        assertEquals(new BigDecimal("0"), result.getCurrentAmount());
        assertEquals(account, result.getAccount());
    }

    @Test
    void contribute_shouldIncreaseCurrentAmount_andReduceBalance() {
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal("1000"));

        SavingsGoal goal = new SavingsGoal();
        goal.setId(10L);
        goal.setAccount(account);
        goal.setTargetAmount(new BigDecimal("500"));
        goal.setCurrentAmount(new BigDecimal("100"));

        ContributeSavingsGoalRequest request = new ContributeSavingsGoalRequest();
        request.setAmount(new BigDecimal("200"));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(savingsGoalRepository.findById(10L)).thenReturn(Optional.of(goal));

        savingsGoalService.contribute(1L, 10L, request);

        assertEquals(new BigDecimal("800"), account.getBalance());
        assertEquals(new BigDecimal("300"), goal.getCurrentAmount());
    }

    @Test
    void contribute_shouldThrowException_whenAccountIsClosed() {
        Account account = new Account();
        account.setId(1L);
        account.setActive(false);

        SavingsGoal goal = new SavingsGoal();
        goal.setId(10L);
        goal.setAccount(account);

        ContributeSavingsGoalRequest request = new ContributeSavingsGoalRequest();
        request.setAmount(new BigDecimal("100"));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(savingsGoalRepository.findById(10L)).thenReturn(Optional.of(goal));

        assertThrows(ClosedAccountException.class, () -> {
            savingsGoalService.contribute(1L, 10L, request);
        });
    }

    @Test
    void contribute_shouldThrowException_whenOwnershipMismatch() {
        Account ownerAccount = new Account();
        ownerAccount.setId(1L);

        Account requestingAccount = new Account();
        requestingAccount.setId(2L);

        SavingsGoal goal = new SavingsGoal();
        goal.setId(10L);
        goal.setAccount(ownerAccount); // belongs to account 1

        ContributeSavingsGoalRequest request = new ContributeSavingsGoalRequest();
        request.setAmount(new BigDecimal("100"));

        when(accountRepository.findById(2L)).thenReturn(Optional.of(requestingAccount));
        when(savingsGoalRepository.findById(10L)).thenReturn(Optional.of(goal));

        assertThrows(SavingsGoalNotFoundException.class, () -> {
            savingsGoalService.contribute(2L, 10L, request); // account 2 asking for account 1's goal
        });
    }

    @Test
    void contribute_shouldThrowException_whenInsufficientFunds() {
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal("50"));

        SavingsGoal goal = new SavingsGoal();
        goal.setId(10L);
        goal.setAccount(account);
        goal.setTargetAmount(new BigDecimal("5000"));
        goal.setCurrentAmount(new BigDecimal("0"));

        ContributeSavingsGoalRequest request = new ContributeSavingsGoalRequest();
        request.setAmount(new BigDecimal("200"));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(savingsGoalRepository.findById(10L)).thenReturn(Optional.of(goal));

        assertThrows(InsufficientFundsException.class, () -> {
            savingsGoalService.contribute(1L, 10L, request);
        });
    }

    @Test
    void contribute_shouldThrowException_whenContributionExceedsTarget() {
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal("10000"));

        SavingsGoal goal = new SavingsGoal();
        goal.setId(10L);
        goal.setAccount(account);
        goal.setTargetAmount(new BigDecimal("500"));
        goal.setCurrentAmount(new BigDecimal("400"));

        ContributeSavingsGoalRequest request = new ContributeSavingsGoalRequest();
        request.setAmount(new BigDecimal("200")); // 400 + 200 = 600, exceeds 500 target

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(savingsGoalRepository.findById(10L)).thenReturn(Optional.of(goal));

        assertThrows(GoalLimitExceedException.class, () -> {
            savingsGoalService.contribute(1L, 10L, request);
        });
    }

    @Test
    void getSavingsGoal_shouldReturnGoal_whenOwnershipIsValid() {
        Account account = new Account();
        account.setId(1L);

        SavingsGoal goal = new SavingsGoal();
        goal.setId(10L);
        goal.setAccount(account);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(savingsGoalRepository.findById(10L)).thenReturn(Optional.of(goal));

        SavingsGoal result = savingsGoalService.getSavingsGoal(1L, 10L);

        assertEquals(10L, result.getId());
    }

    @Test
    void deleteSavingsGoal_shouldSucceed_whenOwnershipIsValid() {
        Account account = new Account();
        account.setId(1L);

        SavingsGoal goal = new SavingsGoal();
        goal.setId(10L);
        goal.setAccount(account);

        when(savingsGoalRepository.findById(10L)).thenReturn(Optional.of(goal));

        savingsGoalService.deleteSavingsGoal(1L, 10L);

        verify(savingsGoalRepository).delete(goal);
    }
}