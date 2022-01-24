package com.trainingdemo.accountsvc.controller;


import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.trainingdemo.accountsvc.domain.Account;
import com.trainingdemo.accountsvc.domain.AccountType;
import com.trainingdemo.accountsvc.domain.Transaction;
import com.trainingdemo.accountsvc.domain.TransactionType;
import com.trainingdemo.accountsvc.dto.CreateTransactionRequestDto;
import com.trainingdemo.accountsvc.repository.AccountRepository;
import org.assertj.core.api.Assertions;
import org.hibernate.dialect.PostgreSQL9Dialect;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.*;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Testcontainers
public class TransactionControllerFlowIT {

	@Container
	static PostgreSQLContainer container = new PostgreSQLContainer("postgres").withUsername("accounts").withPassword("password");

	@Container
	static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:management");

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	TestRestTemplate testRestTemplate;

	@Autowired
	RabbitTemplate rabbitTemplate;

	@DynamicPropertySource
	public static void setup(DynamicPropertyRegistry registry){
		registry.add("spring.datasource.url",()->container.getJdbcUrl());
		registry.add("spring.datasource.username",()->container.getUsername());
		registry.add("spring.datasource.password",()->container.getPassword());
		registry.add("spring.jpa.database-platform", PostgreSQL9Dialect.class::getName);
		registry.add("spring.rabbitmq.host", ()->rabbitMQContainer.getHost());
		registry.add("spring.rabbitmq.port", ()->rabbitMQContainer.getAmqpPort());
	}

	@TestConfiguration
	static class TestConfig {

		//Queue
		@Bean
		public Queue queue(){
			return new Queue("transaction-notification", false);
		}

		//exchange
		@Bean
		public Exchange exchange(){
			return new TopicExchange("transaction-notification");
		}

		//binding
		@Bean
		Binding binding() {
			return BindingBuilder.bind(queue()).to(exchange()).with("#").noargs();
		}

	}


	@Test
	void shouldSaveTransactionToDB(){
		// Arrange
		CreateTransactionRequestDto transactionDto = defaultTransactionDto();
		Account account = new Account();
		//account.setAccountId("ACC1234567");
		account.setCustomerId("CUST12345");
		account.setAccountType(AccountType.SAVINGS);
		account.setBalance(BigDecimal.ZERO);

		Account savedAccount = accountRepository.save(account);
		transactionDto.setAccountId(savedAccount.getAccountId());
		// Act
		// /api/accounts
		HttpEntity entity = new HttpEntity(transactionDto,new HttpHeaders());

		ResponseEntity<Void> exchange = testRestTemplate
				.exchange("/api/transactions",
						HttpMethod.POST, entity, Void.class);
		//Assert

		Assertions.assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		Message receive = rabbitTemplate.receive("transaction-notification", 5000);
		Assertions.assertThat(receive).isNotNull();
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


}
