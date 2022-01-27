package com.trainingdemo.accountsvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trainingdemo.accountsvc.config.TestData;
import com.trainingdemo.accountsvc.dto.CreateTransactionRequestDto;
import com.trainingdemo.accountsvc.service.TransactionService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TransactionService transactionService;

	@MockBean(answer = Answers.RETURNS_DEEP_STUBS)
	MeterRegistry meterRegistry;

    @Autowired
    private ObjectMapper objectMapper;

	@Mock
	Counter counter;

    @Test
    void shouldReturn201AndLocationHeaderWhenTransactionCreatedSuccessfully() throws Exception {

		Mockito.when(meterRegistry.counter(any())).thenReturn(counter);
		Mockito.doNothing().when(counter).increment();
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
