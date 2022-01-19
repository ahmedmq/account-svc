package com.trainingdemo.accountsvc.service;

import com.trainingdemo.accountsvc.domain.Account;
import com.trainingdemo.accountsvc.domain.AccountType;
import com.trainingdemo.accountsvc.dto.AccountDto;
import com.trainingdemo.accountsvc.dto.CreateAccountRequestDto;
import com.trainingdemo.accountsvc.exception.AccountNotFoundException;
import com.trainingdemo.accountsvc.repository.AccountRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    AccountRepository accountRepository;


    AccountService cut;

    @BeforeEach
    public void beforeEach(){
       cut = new AccountService(accountRepository);
    }


    @Test
    @DisplayName("Should Return Account when Customer ID exists")
    void shouldReturnAccountWhenCustomerIdExists(){

        Account account = defaultAccount();

        Mockito.when(accountRepository.findByCustomerId("CUST12345")).thenReturn(Optional.of(account));

        //Act
        AccountDto accountDto = cut.findByCustomerId("CUST12345");

        //Assertions
        Assertions.assertThat(accountDto.getAccountId()).isEqualTo(account.getAccountId());
        Assertions.assertThat(accountDto.getCustomerId()).isEqualTo(account.getCustomerId());
        Assertions.assertThat(accountDto.getAccountType()).isEqualTo(account.getAccountType());
        Assertions.assertThat(accountDto.getBalance()).isEqualTo(account.getBalance());

    }

    @Test
    void shouldThrowExceptionWhenCustomerIdDoesNotExists() {

        Mockito.when(accountRepository.findByCustomerId(any())).thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(AccountNotFoundException.class).isThrownBy(()->cut.findByCustomerId("DOEST_NOT_EXIST"));

    }

    @Test
    void shouldReturnAccountIDForSaveNewAccountRequest() {

        Account account = defaultAccount();
        CreateAccountRequestDto createAccountRequestDto = new CreateAccountRequestDto();
        createAccountRequestDto.setCustomerId("CUST12345");
        createAccountRequestDto.setAccountType(AccountType.SAVINGS);

        Mockito.when(accountRepository.save(any())).thenReturn(account);

        String accountId = cut.saveAccount(createAccountRequestDto);

        Assertions.assertThat(accountId).isEqualTo("ACC1234567");


    }

    private Account defaultAccount(){
        Account account = new Account();
        account.setAccountId("ACC1234567");
        account.setCustomerId("CUST12345");
        account.setAccountType(AccountType.SAVINGS);
        account.setBalance(BigDecimal.ZERO);

        return account;
    }


}
