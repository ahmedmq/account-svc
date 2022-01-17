package com.trainingdemo.accountsvc.repository;

import com.trainingdemo.accountsvc.domain.Account;
import com.trainingdemo.accountsvc.domain.AccountType;
import com.trainingdemo.accountsvc.repository.AccountRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.Optional;

@DataJpaTest
public class AccountRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    AccountRepository accountRepository;


    @Test
    //shouldWhen
    void shouldReturnAccountWhenExistingCustomerIdExists() {

        // arrange / given
        Account account = new Account();
        account.setAccountType(AccountType.CURRENT);
        account.setCustomerId("CUST12345");
        account.setBalance(BigDecimal.ZERO);

        testEntityManager.persistAndFlush(account);

        //act / when
        Optional<Account> retrievedAccount = accountRepository.findByCustomerId("CUST12345");


        //assert / then
        Assertions.assertThat(retrievedAccount).isPresent();
        Assertions.assertThat(retrievedAccount.get().getAccountId()).isNotNull();
        Assertions.assertThat(retrievedAccount.get().getCustomerId()).isEqualTo("CUST12345");
        Assertions.assertThat(retrievedAccount.get().getAccountType()).isEqualTo(AccountType.CURRENT);
        Assertions.assertThat(retrievedAccount.get().getBalance()).isEqualTo(BigDecimal.ZERO);

    }
}
