package com.example.demo;

import com.example.demo.controller.TransactionController;
import com.example.demo.domain.Transaction;
import com.example.demo.domain.Type;
import com.example.demo.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.util.UUID;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private TransactionController transactionController;

	@Override
	public void run(String... args) throws Exception {
		for (int i = 0 ; i < 20; i++) {
			Transaction t = new Transaction(UUID.randomUUID(), UUID.randomUUID(), "GBP", new BigDecimal("10.5"), "desc", Type.CREDIT);
			transactionController.save(t);
			Transaction t1 = new Transaction(UUID.randomUUID(), UUID.randomUUID(), "GBP", new BigDecimal("20.5"), "desc", Type.CREDIT);
			transactionController.save(t1);

			Transaction t2 = new Transaction(UUID.randomUUID(), UUID.randomUUID(), "GBP", new BigDecimal("30.5"), "desc", Type.CREDIT);
			transactionController.save(t2);

			Transaction t3 = new Transaction(UUID.randomUUID(), UUID.randomUUID(), "GBP", new BigDecimal("30.5"), "desc", Type.DEBIT);
			transactionController.save(t3);

			Transaction t4 = new Transaction(UUID.randomUUID(), UUID.randomUUID(), "GBP", new BigDecimal("20.5"), "desc", Type.DEBIT);
			transactionController.save(t4);

			Transaction t5 = new Transaction(UUID.randomUUID(), UUID.randomUUID(), "GBP", new BigDecimal("10.5"), "desc", Type.DEBIT);
			transactionController.save(t5);

		}
	}
}
