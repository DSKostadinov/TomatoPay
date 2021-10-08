package com.example.demo.service;

import com.example.demo.domain.Transaction;
import com.example.demo.domain.Type;
import com.example.demo.exception.TransactionNotFoundException;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    TransactionRepository transactionRepository;

    @InjectMocks
    TransactionServiceImpl transactionService;

    @Test
    void testFindById() throws ExecutionException, InterruptedException {
        UUID uuid = UUID.randomUUID();
        Transaction transaction = new Transaction(uuid, uuid, "GBP", new BigDecimal(10), "desc", Type.DEBIT);
        when(transactionRepository.findById(transaction.getAccountId()))
                .thenReturn(Optional.of(transaction));
        assertNotNull(transactionService.findById(transaction.getAccountId()));
    }

    @Test
    void testDeleteByid() throws TransactionNotFoundException {
        UUID uuid = UUID.randomUUID();
        Transaction transaction = new Transaction(uuid, uuid, "GBP", new BigDecimal(10), "desc", Type.DEBIT);
        transactionRepository.save(transaction);
        transactionRepository.deleteById(uuid);

        assertThrows(TransactionNotFoundException.class, () ->
                transactionService.findById(uuid)
        );
    }

    @Test
    void testFindAll() {
        UUID uuid = UUID.randomUUID();
        Transaction transaction = new Transaction(uuid, uuid, "GBP", new BigDecimal(10), "desc", Type.DEBIT);

        when(transactionRepository.findAll(PageRequest.of(0, 8))).thenReturn(new PageImpl<>(List.of(transaction, transaction)));
        Page<Transaction> page = transactionService.findAll(PageRequest.of(0, 8));
        assertEquals(page.getTotalElements(), 2);
    }

    @Test
    void testSave() {
        UUID uuid = UUID.randomUUID();
        Transaction transaction = new Transaction(uuid, uuid, "GBP", new BigDecimal(10), "desc", Type.DEBIT);
        when(transactionRepository.save(transaction)).thenReturn(transaction);
        Transaction t = transactionService.save(transaction);

        assertEquals(t.getId(), transaction.getId());
        assertEquals(transactionService.getSum(), BigDecimal.valueOf(10));

        uuid = UUID.randomUUID();
        transaction = new Transaction(uuid, uuid, "GBP", new BigDecimal(10), "desc", Type.CREDIT);
        when(transactionRepository.save(transaction)).thenReturn(transaction);
        t = transactionService.save(transaction);

        assertEquals(t.getId(), transaction.getId());
        assertEquals(transactionService.getSum(), BigDecimal.valueOf(0));
    }
}
