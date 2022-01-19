package com.trainingdemo.accountsvc.controller;

import com.trainingdemo.accountsvc.dto.CreateTransactionRequestDto;
import com.trainingdemo.accountsvc.service.TransactionService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/api/transactions")
    public ResponseEntity<Void> createTransaction(@RequestBody CreateTransactionRequestDto createTransactionRequestDto, UriComponentsBuilder uriComponentsBuilder){
        Long transactionId = transactionService.saveTransaction(createTransactionRequestDto);
        HttpHeaders headers = new HttpHeaders();
        UriComponents location = uriComponentsBuilder.path("/api/transactions/{transactionId}").buildAndExpand(transactionId);
        headers.setLocation(location.toUri());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }
}
