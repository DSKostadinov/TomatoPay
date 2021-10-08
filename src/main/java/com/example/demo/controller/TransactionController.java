package com.example.demo.controller;

import com.example.demo.domain.Transaction;
import com.example.demo.domain.request.CreateTransactionRequest;
import com.example.demo.service.TransactionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/transactions")
@Api(tags = "Transaction APIs")
public class TransactionController {

    private static final Logger LOGGER = LogManager.getLogger();

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @ApiResponses(
            value = {@ApiResponse(code = 200, message = "Retrieved transaction")
            }
    )
    @GetMapping("/{transactionId}")
    public Transaction getTransaction(@PathVariable UUID transactionId) {
        return transactionService.findById(transactionId);
    }

    @ApiResponses(
            value = {@ApiResponse(code = 200, message = "Retrieved all transactions")
            }
    )
    @GetMapping
    public Page<Transaction> getAllTransactions(Pageable pageable) {
        return transactionService.findAll(pageable);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(
            value = {@ApiResponse(code = 201, message = "Transaction created")
            }
    )
    @PostMapping
    public CompletableFuture<Transaction> createTransaction(@Valid @RequestBody CreateTransactionRequest createTransactionRequest) {
        LOGGER.info("Async request to create transaction received");
        Transaction transaction = toTransaction(createTransactionRequest);
        transactionService.save(transaction);

        return CompletableFuture.completedFuture(transaction);
    }

    @ApiResponses(
            value = {@ApiResponse(code = 200, message = "Transaction updated")
            }
    )
    @PutMapping("/{transactionId}")
    public Transaction updateTransaction(@PathVariable UUID transactionId, @RequestParam String newDesc) {
        Transaction transaction = transactionService.findById(transactionId);
        transaction.setDescription(newDesc);
        
        return transactionService.save(transaction);;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(
            value = {@ApiResponse(code = 204, message = "Transaction deleted")
            }
    )
    @DeleteMapping("/{transactionId}")
    public void deleteTransaction(@PathVariable UUID transactionId) {
        transactionService.deleteById(transactionId);
    }

    private Transaction toTransaction(CreateTransactionRequest createTransactionRequest) {
        Transaction transaction = new Transaction();
        transaction.setId(UUID.randomUUID());
        transaction.setAmount(createTransactionRequest.getAmount());
        transaction.setAccountId(UUID.fromString(createTransactionRequest.getAccountId()));
        transaction.setCurrency(createTransactionRequest.getCurrency());
        transaction.setDescription(createTransactionRequest.getDescription());
        transaction.setType(createTransactionRequest.getType());

        return transaction;
    }

    public CompletableFuture<Transaction> save(Transaction transaction) {
        LOGGER.info("Async request to create transaction received");
        transactionService.save(transaction);

        return CompletableFuture.completedFuture(transaction);
    }
}
