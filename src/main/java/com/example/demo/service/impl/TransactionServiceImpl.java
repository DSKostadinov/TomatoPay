package com.example.demo.service.impl;

import com.example.demo.domain.Transaction;
import com.example.demo.domain.Type;
import com.example.demo.exception.TransactionNotFoundException;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.service.TransactionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

@Service("transactionService")
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private static final Logger LOGGER = LogManager.getLogger();

    private BigDecimal sum;
    private final TransactionRepository transactionRepository;
    private final ReentrantLock reentrantLock;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
        sum = BigDecimal.valueOf(0);
        reentrantLock = new ReentrantLock();
    }

    @Override
    public Transaction findById(UUID transactionId) {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException(
                        String.format("Transaction with id %s not found", transactionId)
                ));
    }

    @Override
    public Page<Transaction> findAll(Pageable pageable) {
        return transactionRepository.findAll(pageable);
    }

    @Override
    @Async("executor")
    public Transaction save(Transaction transaction) {
        transactionRepository.save(transaction);

        //for the purpose of showing async operations
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        reentrantLock.lock();

        try {
            BigDecimal oldValue = sum;

            if (transaction.getType() == Type.DEBIT) {
                sum = sum.add(transaction.getAmount());
            } else {
                sum = sum.subtract(transaction.getAmount());
            }

            LOGGER.info("Transaction is of type {} and amount is {}", transaction.getType(), transaction.getAmount());
            LOGGER.info("Price updated from {} to {}", oldValue, sum);
        } finally {
            reentrantLock.unlock();
        }

        return transaction;
    }

    @Override
    public void deleteById(UUID transactionId) {
        transactionRepository.deleteById(transactionId);
    }

    public BigDecimal getSum() {
        return sum;
    }
}
