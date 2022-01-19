package com.trainingdemo.accountsvc.controller;

import com.trainingdemo.accountsvc.domain.Account;
import com.trainingdemo.accountsvc.domain.AccountType;
import com.trainingdemo.accountsvc.dto.AccountDto;
import com.trainingdemo.accountsvc.dto.CreateAccountRequestDto;
import com.trainingdemo.accountsvc.repository.AccountRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.math.BigDecimal;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountControllerIT {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TestRestTemplate testRestTemplate;

    @Test
    void shouldReturnAccountForCustomerIdExist(){
        // Arrange
        Account account = new Account();
        //account.setAccountId("ACC1234567");
        account.setCustomerId("CUST12345");
        account.setAccountType(AccountType.SAVINGS);
        account.setBalance(BigDecimal.ZERO);

        Account savedAccount = accountRepository.save(account);

        // Act
        // /api/accounts?customerId


        ResponseEntity<AccountDto> exchange = testRestTemplate
                .exchange("/api/accounts?customerId=" + "CUST12345",
                        HttpMethod.GET, null, AccountDto.class);


        //Assert

        Assertions.assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(exchange.getBody()).isNotNull();

        Assertions.assertThat(exchange.getBody().getBalance().stripTrailingZeros()).isEqualTo(account.getBalance().stripTrailingZeros());
        Assertions.assertThat(exchange.getBody().getCustomerId()).isEqualTo(account.getCustomerId());
        Assertions.assertThat(exchange.getBody().getAccountType()).isEqualTo(account.getAccountType());


    }

    @Test
    void shouldSaveAccountToDB(){
        // Arrange
        CreateAccountRequestDto account = new CreateAccountRequestDto();
        account.setCustomerId("CUST12345");
        account.setAccountType(AccountType.SAVINGS);

       // Account savedAccount = accountRepository.save(account);

        // Act
        // /api/accounts
        HttpEntity entity = new HttpEntity(account,new HttpHeaders());

        ResponseEntity<Void> exchange = testRestTemplate
                .exchange("/api/accounts",
                        HttpMethod.POST, entity, Void.class);
        //Assert

        Assertions.assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(exchange.getHeaders().getLocation()).isNotNull();



    }

}
