package com.trainingdemo.accountsvc.config;

import com.trainingdemo.accountsvc.domain.TransactionType;
import com.trainingdemo.accountsvc.dto.CreateTransactionRequestDto;

import java.math.BigDecimal;

public class TestData {

    public static CreateTransactionRequestDto defaultTransactionDto() {
        CreateTransactionRequestDto transactionDto = new CreateTransactionRequestDto();
        transactionDto.setTransactionType(TransactionType.DEPOSIT);
        transactionDto.setAmount(BigDecimal.TEN);
        transactionDto.setDescription("Happy Birthday");
        transactionDto.setAccountId("ACC1234567");

        return transactionDto;
    }

}
