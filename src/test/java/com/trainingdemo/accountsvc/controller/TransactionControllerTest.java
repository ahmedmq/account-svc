package com.trainingdemo.accountsvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trainingdemo.accountsvc.config.TestData;
import com.trainingdemo.accountsvc.dto.CreateTransactionRequestDto;
import com.trainingdemo.accountsvc.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturn201AndLocationHeaderWhenTransactionCreatedSuccessfully() throws Exception {

        Mockito.when(transactionService.saveTransaction(any())).thenReturn(1L);
        CreateTransactionRequestDto createTransactionRequestDto = TestData.defaultTransactionDto();
        String json = objectMapper.writeValueAsString(createTransactionRequestDto);
        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"));


    }


}
