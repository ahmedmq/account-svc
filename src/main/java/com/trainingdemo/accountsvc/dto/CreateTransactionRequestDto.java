package com.trainingdemo.accountsvc.dto;

import com.trainingdemo.accountsvc.domain.AccountType;
import com.trainingdemo.accountsvc.domain.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CreateTransactionRequestDto {

    private String accountId;
    private BigDecimal amount;
    private String description;
    private TransactionType transactionType;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
}
