package com.trainingdemo.accountsvc.repository;

import com.trainingdemo.accountsvc.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,String> {
    Optional<Account> findByCustomerId(String customerId);
}
