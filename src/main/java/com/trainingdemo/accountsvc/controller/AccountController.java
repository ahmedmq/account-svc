package com.trainingdemo.accountsvc.controller;

import com.trainingdemo.accountsvc.dto.AccountDto;
import com.trainingdemo.accountsvc.dto.CreateAccountRequestDto;
import com.trainingdemo.accountsvc.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(summary = "Retrieve an account for a given customer", description = "API to get the account details for an existing customer")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Return sucessful acount for a existing customerid")
    })
    @GetMapping("/api/accounts")
    public AccountDto getAccountByCustomer(@RequestParam String customerId){
        AccountDto accountDto = accountService.findByCustomerId(customerId);
        return accountDto;
    }

    @PostMapping("/api/accounts")
    public ResponseEntity<Void> saveAccount(@RequestBody CreateAccountRequestDto createAccountRequestDto, UriComponentsBuilder uriComponentsBuilder){
        String accountId = accountService.saveAccount(createAccountRequestDto);
        UriComponents uriComponents = uriComponentsBuilder.path("/api/account/{id}").buildAndExpand(accountId);
        // api/account/{id}
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriComponents.toUri());
        return new ResponseEntity(headers,HttpStatus.CREATED);
    }

}
