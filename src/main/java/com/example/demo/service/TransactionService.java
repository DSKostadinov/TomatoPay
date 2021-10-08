package com.example.demo.service;

import com.example.demo.domain.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TransactionService {

    Transaction findById(UUID transctionId);

    Page<Transaction> findAll(Pageable pageable);

    Transaction save(Transaction transaction);

    void deleteById(UUID transactionId);
}
