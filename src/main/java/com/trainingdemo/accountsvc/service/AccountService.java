package com.trainingdemo.accountsvc.service;

import com.trainingdemo.accountsvc.domain.Account;
import com.trainingdemo.accountsvc.dto.AccountDto;
import com.trainingdemo.accountsvc.dto.CreateAccountRequestDto;
import com.trainingdemo.accountsvc.exception.AccountNotFoundException;
import com.trainingdemo.accountsvc.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class AccountService {
    private AccountRepository accountRepository;
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository=accountRepository;
    }

    public AccountDto findByCustomerId(String customerId) {
        Optional<Account> account= accountRepository.findByCustomerId(customerId);
        Account accountEntity = account.orElseThrow(AccountNotFoundException::new);
        return toAccountDto(accountEntity);
    }

    private AccountDto toAccountDto(Account account){
        AccountDto dto = new AccountDto();
        dto.setAccountId(account.getAccountId());
        dto.setCustomerId(account.getCustomerId());
        dto.setAccountType(account.getAccountType());
        dto.setBalance(account.getBalance());
        return dto;
    }

    private Account toAccountEntity(CreateAccountRequestDto createAccountRequestDto){
        Account account = new Account();
        account.setCustomerId(createAccountRequestDto.getCustomerId());
        account.setAccountType(createAccountRequestDto.getAccountType());
        account.setBalance(BigDecimal.ZERO);
        return account;
    }

    public String saveAccount(CreateAccountRequestDto createAccountRequestDto) {
        Account account = toAccountEntity(createAccountRequestDto);
        Account accountEntity = accountRepository.save(account);
        return accountEntity.getAccountId();
    }
}
