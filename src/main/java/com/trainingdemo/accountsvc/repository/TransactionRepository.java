package com.trainingdemo.accountsvc.repository;

import com.trainingdemo.accountsvc.domain.Account;
import com.trainingdemo.accountsvc.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {

}
