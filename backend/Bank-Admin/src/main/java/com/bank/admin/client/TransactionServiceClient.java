package com.bank.admin.client;

import java.math.BigDecimal;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.bank.admin.config.FeignConfig;

@FeignClient(
        name = "TRANSACTION-SERVICE",
        		url = "http://localhost:8086",
        configuration = FeignConfig.class
)
public interface TransactionServiceClient {
	@GetMapping("/api/transactions/internal/dashboard/total")
	Long getTotalTransactions();

	@GetMapping("/api/transactions/internal/dashboard/successful")
	Long getSuccessfulTransactions();

	@GetMapping("/api/transactions/internal/dashboard/failed")
	Long getFailedTransactions();

	@GetMapping("/api/transactions/internal/dashboard/amount")
	BigDecimal getTotalTransactionAmount();

}