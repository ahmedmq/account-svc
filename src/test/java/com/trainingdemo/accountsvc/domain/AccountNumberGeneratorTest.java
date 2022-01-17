package com.trainingdemo.accountsvc.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountNumberGeneratorTest {

    AccountNumberGenerator cut = new AccountNumberGenerator();


    @Test
    void shouldReturnCorrectAccountIdWhenRequested(){

        // ACC1234567

        //Arrange
        //Act
        String accountId = cut.generate(null, null).toString();

        //Assert
        Assertions.assertThat(accountId).isNotNull();
        Assertions.assertThat(accountId).matches("[ACC0-9]+");

    }


}