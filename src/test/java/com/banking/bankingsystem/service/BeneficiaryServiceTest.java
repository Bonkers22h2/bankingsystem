package com.banking.bankingsystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.banking.bankingsystem.dto.CreateBeneficiaryRequest;
import com.banking.bankingsystem.dto.UpdateBeneficiaryRequest;
import com.banking.bankingsystem.exception.AccountNotFoundException;
import com.banking.bankingsystem.exception.BeneficiaryNotFoundException;
import com.banking.bankingsystem.exception.ClosedAccountException;
import com.banking.bankingsystem.model.Account;
import com.banking.bankingsystem.model.Beneficiary;
import com.banking.bankingsystem.repository.AccountRepository;
import com.banking.bankingsystem.repository.BeneficiaryRepository;

@ExtendWith(MockitoExtension.class)
class BeneficiaryServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private BeneficiaryRepository beneficiaryRepository;

    @InjectMocks
    private BeneficiaryService beneficiaryService;

    @Test
    void getBeneficiary_shouldReturnBeneficiary_whenOwnershipIsValid() {
        Account account = new Account();
        account.setId(1L);

        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setId(10L);
        beneficiary.setAccount(account);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(beneficiaryRepository.findById(10L)).thenReturn(Optional.of(beneficiary));

        Beneficiary result = beneficiaryService.getBeneficiary(1L, 10L);

        assertEquals(10L, result.getId());
    }

    @Test
    void getBeneficiary_shouldThrowException_whenAccountNotFound() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> {
            beneficiaryService.getBeneficiary(1L, 10L);
        });
    }

    @Test
    void getBeneficiary_shouldThrowException_whenBeneficiaryNotFound() {
        Account account = new Account();
        account.setId(1L);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(beneficiaryRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> {
            beneficiaryService.getBeneficiary(1L, 10L);
        });
    }

    @Test
    void getBeneficiary_shouldThrowException_whenAccountIsClosed() {
        Account account = new Account();
        account.setId(1L);
        account.setActive(false);

        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setId(10L);
        beneficiary.setAccount(account);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(beneficiaryRepository.findById(10L)).thenReturn(Optional.of(beneficiary));

        assertThrows(ClosedAccountException.class, () -> {
            beneficiaryService.getBeneficiary(1L, 10L);
        });
    }

    @Test
    void getBeneficiary_shouldThrowException_whenOwnershipMismatch() {
        Account ownerAccount = new Account();
        ownerAccount.setId(1L);

        Account requestingAccount = new Account();
        requestingAccount.setId(2L);

        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setId(10L);
        beneficiary.setAccount(ownerAccount); // belongs to account 1

        when(accountRepository.findById(2L)).thenReturn(Optional.of(requestingAccount));
        when(beneficiaryRepository.findById(10L)).thenReturn(Optional.of(beneficiary));

        assertThrows(BeneficiaryNotFoundException.class, () -> {
            beneficiaryService.getBeneficiary(2L, 10L); // account 2 asking for account 1's beneficiary
        });
    }

    @Test
    void createBeneficiary_shouldSaveWithCorrectAccount() {
        Account account = new Account();
        account.setId(1L);

        CreateBeneficiaryRequest request = new CreateBeneficiaryRequest();
        request.setNickname("Mom");
        request.setBeneficiaryAccountNumber("1002");

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(beneficiaryRepository.save(org.mockito.ArgumentMatchers.any(Beneficiary.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Beneficiary result = beneficiaryService.createBeneficiary(1L, request);

        assertEquals("Mom", result.getNickname());
        assertEquals(account, result.getAccount());
    }

    @Test
    void updateBeneficiary_shouldThrowException_whenOwnershipMismatch() {
        Account ownerAccount = new Account();
        ownerAccount.setId(1L);

        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setId(10L);
        beneficiary.setAccount(ownerAccount);

        UpdateBeneficiaryRequest request = new UpdateBeneficiaryRequest();
        request.setNickname("New Name");
        request.setBeneficiaryAccountNumber("9999");

        when(beneficiaryRepository.findById(10L)).thenReturn(Optional.of(beneficiary));

        assertThrows(BeneficiaryNotFoundException.class, () -> {
            beneficiaryService.updateBeneficiary(2L, 10L, request); // wrong accountId
        });
    }

    @Test
    void deleteBeneficiary_shouldSucceed_whenOwnershipIsValid() {
        Account account = new Account();
        account.setId(1L);

        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setId(10L);
        beneficiary.setAccount(account);

        when(beneficiaryRepository.findById(10L)).thenReturn(Optional.of(beneficiary));

        beneficiaryService.deleteBeneficiary(1L, 10L);

        verify(beneficiaryRepository).delete(beneficiary);
    }
}