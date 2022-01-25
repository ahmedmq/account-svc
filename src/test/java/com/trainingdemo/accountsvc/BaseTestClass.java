package com.trainingdemo.accountsvc;

import com.trainingdemo.accountsvc.domain.TransactionType;
import com.trainingdemo.accountsvc.dto.TransactionDto;
import com.trainingdemo.accountsvc.service.TransactionService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.anyLong;

@SpringBootTest(properties = "spring.flyway.enabled=false")
@ActiveProfiles("integration-test")
public class BaseTestClass {

    @Autowired
    WebApplicationContext webApplicationContext;

    @MockBean
    TransactionService transactionService;

    @BeforeEach
    void shouldReturnTransactionDetails(){
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setAccountId("ACC1234567");
        transactionDto.setTransactionId(9999L);
        transactionDto.setTransactionType(TransactionType.DEPOSIT);
        transactionDto.setAmount(BigDecimal.valueOf(99));
        transactionDto.setBalance(BigDecimal.valueOf(100));
        transactionDto.setDescription("Token Amount");
        transactionDto.setCreateAt("2022-01-01T01:01:01");

        Mockito.when(transactionService.getTransaction(anyLong())).thenReturn(transactionDto);
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }

}
