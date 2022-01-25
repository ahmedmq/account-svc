package com.trainingdemo.accountsvc.service;

import com.trainingdemo.accountsvc.domain.Account;
import com.trainingdemo.accountsvc.domain.Transaction;
import com.trainingdemo.accountsvc.domain.TransactionType;
import com.trainingdemo.accountsvc.dto.CreateAccountRequestDto;
import com.trainingdemo.accountsvc.dto.CreateTransactionRequestDto;
import com.trainingdemo.accountsvc.dto.TransactionDto;
import com.trainingdemo.accountsvc.dto.TransactionNotificationDto;
import com.trainingdemo.accountsvc.exception.AccountNotFoundException;
import com.trainingdemo.accountsvc.repository.AccountRepository;
import com.trainingdemo.accountsvc.repository.TransactionRepository;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TransactionService {

    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;
	private StreamBridge streamBridge;

    public TransactionService(TransactionRepository transactionRepository,AccountRepository accountRepository,StreamBridge streamBridge
			) {
        this.transactionRepository= transactionRepository;
        this.accountRepository=accountRepository;
		this.streamBridge=streamBridge;
    }

    public Long saveTransaction(CreateTransactionRequestDto createTransactionRequestDto) {
        // Get account details
        Optional<Account> account = accountRepository.findById(createTransactionRequestDto.getAccountId());
        Account accountEntity = account.orElseThrow(AccountNotFoundException::new);
        accountEntity.setBalance(accountEntity.getBalance().add(createTransactionRequestDto.getAmount()));
        // Save account details with updated balance
        accountRepository.save(accountEntity);

        // Save transaction
        Transaction transaction = toTransactionEntity(createTransactionRequestDto);
        Transaction transactionEntity = transactionRepository.save(transaction);
		TransactionNotificationDto transactionNotificationDto = new TransactionNotificationDto(transactionEntity.getTransactionId());
		// Sending the notification via messaging
		streamBridge.send("transaction-notification", MessageBuilder.withPayload(transactionNotificationDto).build());
        return transactionEntity.getTransactionId();
    }

    private Transaction toTransactionEntity(CreateTransactionRequestDto createTransactionRequestDto) {
        Transaction result = new Transaction();

        result.setAccountId(createTransactionRequestDto.getAccountId());
        result.setAmount(createTransactionRequestDto.getAmount());
        result.setDescription(createTransactionRequestDto.getDescription());
        result.setTransactionType(createTransactionRequestDto.getTransactionType());
        result.setTransactionDate(LocalDateTime.now());

        return result;
    }

    public TransactionDto getTransaction(long anyLong) {
        return null;
    }
}
