package com.bank.transactions.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.transactions.dto.TransactionResponseDto;
import com.bank.transactions.dto.TransferRequestDto;
import com.bank.transactions.enums.TransactionStatus;
import com.bank.transactions.service.TransactionService;

import jakarta.validation.Valid;

/**
 * REST entry point for the Transfer Flow (B2).
 * Only the transfer endpoint is exposed — deposit, withdraw, and history are
 * explicitly out of scope for this controller.
 */
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponseDto> transfer(@Valid @RequestBody TransferRequestDto request) {
        TransactionResponseDto response = transactionService.transfer(request);

        HttpStatus httpStatus = (response.getStatus() == TransactionStatus.SUCCESS)
                ? HttpStatus.OK
                : HttpStatus.ACCEPTED; // FLAGGED — request was understood but not executed

        return ResponseEntity.status(httpStatus).body(response);
    }
}
