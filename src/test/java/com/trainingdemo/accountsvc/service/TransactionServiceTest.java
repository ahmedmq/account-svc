package com.trainingdemo.accountsvc.service;

import com.trainingdemo.accountsvc.domain.Account;
import com.trainingdemo.accountsvc.domain.AccountType;
import com.trainingdemo.accountsvc.domain.Transaction;
import com.trainingdemo.accountsvc.domain.TransactionType;
import com.trainingdemo.accountsvc.dto.AccountDto;
import com.trainingdemo.accountsvc.dto.CreateAccountRequestDto;
import com.trainingdemo.accountsvc.dto.CreateTransactionRequestDto;
import com.trainingdemo.accountsvc.dto.TransactionNotificationDto;
import com.trainingdemo.accountsvc.exception.AccountNotFoundException;
import com.trainingdemo.accountsvc.repository.AccountRepository;
import com.trainingdemo.accountsvc.repository.TransactionRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

	@Mock
	private StreamBridge streamBridge;

    TransactionService cut;

    @BeforeEach
    public void beforeEach(){
        cut = new TransactionService(transactionRepository,accountRepository,streamBridge);

    }

    @Test
    @DisplayName("Should Crate a new Transaction and Update the Account Balance, and return new transactionId")
    void shouldCreateNewTransactionAndUpdateAccountBalance(){

        Transaction transaction = defaultTransaction();
        Account account = defaultAccount();

        ArgumentCaptor<Account> argumentCaptor = ArgumentCaptor.forClass(Account.class);
        ArgumentCaptor<Message<TransactionNotificationDto>> argumentCaptorNotification = ArgumentCaptor.forClass(Message.class);


        Mockito.when(accountRepository.findById(any())).thenReturn(Optional.of(account));

        Mockito.when(transactionRepository.save(any())).thenReturn(transaction);


        // accountRespository.save(Account entity)
        // Capture entity - entity.getBalance = actual balance + deposity


        //Act
        CreateTransactionRequestDto transactionDto = defaultTransactionDto();
        Long savedTxDto = cut.saveTransaction(transactionDto);
        Mockito.verify(accountRepository, Mockito.times(1)).save(argumentCaptor.capture());
		Mockito.verify(streamBridge,Mockito.times(1)).send(any(),argumentCaptorNotification.capture());
        //Assertions
        Assertions.assertThat(savedTxDto).isNotNull();
        Account capturedAccount = argumentCaptor.getValue();
		Message<TransactionNotificationDto> transactionNotificationDto = argumentCaptorNotification.getValue();
		Assertions.assertThat(transactionNotificationDto.getPayload().getTransactionId()).isNotNull();
        Assertions.assertThat(capturedAccount.getBalance().stripTrailingZeros()).isEqualTo(BigDecimal.TEN.stripTrailingZeros());

    }

    @Test
    @DisplayName("Should throw an errof if the account does not exist")
    void shouldThrowErrorIfAccountDoesNotExist(){

        Transaction transaction = defaultTransaction();
        Mockito.when(accountRepository.findById(any())).thenReturn(Optional.empty());
        //Act
        CreateTransactionRequestDto transactionDto = defaultTransactionDto();
        Assertions.assertThatExceptionOfType(AccountNotFoundException.class).isThrownBy(()->cut.saveTransaction(transactionDto));

    }

    private Transaction defaultTransaction() {
        Transaction transaction = new Transaction();
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setAmount(BigDecimal.TEN);
        transaction.setDescription("Happy Birthday");
        transaction.setTransactionId(1L);
        transaction.setAccountId("ACC1234567");

        return transaction;
    }

    private CreateTransactionRequestDto defaultTransactionDto() {
        CreateTransactionRequestDto transactionDto = new CreateTransactionRequestDto();
        transactionDto.setTransactionType(TransactionType.DEPOSIT);
        transactionDto.setAmount(BigDecimal.TEN);
        transactionDto.setDescription("Happy Birthday");
        transactionDto.setAccountId("ACC1234567");

        return transactionDto;
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
