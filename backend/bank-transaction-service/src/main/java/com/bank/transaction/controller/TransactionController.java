package com.bank.transaction.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.transaction.dto.request.DepositRequest;
import com.bank.transaction.dto.request.WithdrawRequest;
import com.bank.transaction.dto.response.TransactionResponse;
import com.bank.transaction.service.TransactionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Validated
public class TransactionController {
	
	private final TransactionService transactionService;
	
	public TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}
	
	@PostMapping("/deposit")
	public ResponseEntity<TransactionResponse> deposit(
			@Valid @RequestBody DepositRequest request) {
		
		TransactionResponse response = transactionService.deposit(request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping("/withdraw")
	public ResponseEntity<TransactionResponse> withdraw(
			@Valid @RequestBody WithdrawRequest request) {
		
		TransactionResponse response = transactionService.withdraw(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/{transactionId}")
	public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable Integer transactionId){
		TransactionResponse response = transactionService.getTransactionById(transactionId);
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/account/{accountId}")
	public ResponseEntity<List<TransactionResponse>> getTransactionHistory(@PathVariable Integer accountId) {
		List<TransactionResponse> response = transactionService.getTransactionHistory(accountId);
		
		return ResponseEntity.ok(response);
	}	
}
