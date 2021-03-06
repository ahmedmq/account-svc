package com.trainingdemo.accountsvc.controller;

import com.trainingdemo.accountsvc.dto.CreateTransactionRequestDto;
import com.trainingdemo.accountsvc.dto.TransactionDto;
import com.trainingdemo.accountsvc.service.TransactionService;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class TransactionController {

    private final TransactionService transactionService;
    private final MeterRegistry meterRegistry;

    public TransactionController(TransactionService transactionService,MeterRegistry meterRegistry) {
        this.transactionService = transactionService;
        this.meterRegistry = meterRegistry;
    }

    @PostMapping("/api/transactions")
    public ResponseEntity<Void> createTransaction(@RequestBody CreateTransactionRequestDto createTransactionRequestDto, UriComponentsBuilder uriComponentsBuilder){
        meterRegistry.counter("CreateTransaction").increment();
        Long transactionId = transactionService.saveTransaction(createTransactionRequestDto);
        HttpHeaders headers = new HttpHeaders();
        UriComponents location = uriComponentsBuilder.path("/api/transactions/{transactionId}").buildAndExpand(transactionId);
        headers.setLocation(location.toUri());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @GetMapping("/api/transactions/{transactionId}")
    public TransactionDto getTransaction(@PathVariable Long transactionId){
        return transactionService.getTransaction(transactionId);
    }
}
