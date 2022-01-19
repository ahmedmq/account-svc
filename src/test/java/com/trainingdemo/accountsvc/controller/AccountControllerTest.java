package com.trainingdemo.accountsvc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trainingdemo.accountsvc.domain.AccountType;
import com.trainingdemo.accountsvc.dto.AccountDto;
import com.trainingdemo.accountsvc.dto.CreateAccountRequestDto;
import com.trainingdemo.accountsvc.exception.AccountNotFoundException;
import com.trainingdemo.accountsvc.service.AccountService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AccountService accountService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void shouldReturn200OKForGetAccountByCustomerId() throws Exception {

        AccountDto accountDto = new AccountDto();
        accountDto.setAccountId("ACC1234567");
        accountDto.setCustomerId("CUST12345");
        accountDto.setAccountType(AccountType.SAVINGS);
        accountDto.setBalance(BigDecimal.ZERO);

        Mockito.when(accountService.findByCustomerId("CUST12345")).thenReturn(accountDto);

        String expectedJson = objectMapper.writeValueAsString(accountDto);

        // /api/accounts?customerId=CUS12345
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/accounts")
                        .queryParam("customerId", "CUST12345"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedJson));
    }

    @Test
    void shouldReturn404ForGetAccountByCustomerIdDoesNotExists() throws Exception {

        Mockito.when(accountService.findByCustomerId("DUMMY")).thenThrow(AccountNotFoundException.class);

        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/accounts")
                        .queryParam("customerId", "DUMMY"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void shouldReturn201AndLocationHeaderWhenAccountCreatedSuccessfully() throws Exception {

        Mockito.when(accountService.saveAccount(any())).thenReturn("ACC1234567");

        CreateAccountRequestDto createAccountRequestDto = new CreateAccountRequestDto();
        createAccountRequestDto.setAccountType(AccountType.CURRENT);
        createAccountRequestDto.setCustomerId("CUST12345");

        String json = objectMapper.writeValueAsString(createAccountRequestDto);

        mockMvc
                .perform(MockMvcRequestBuilders
                .post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"));


    }
}
