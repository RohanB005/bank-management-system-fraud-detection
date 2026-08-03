package com.bank.transactions.service;

import com.bank.transactions.dto.TransactionResponseDto;
import com.bank.transactions.dto.TransferRequestDto;

/**
 * Service-layer contract for transaction processing.
 * Only the Transfer Flow (B2) is in scope — no deposit, withdraw, or history operations.
 */
public interface TransactionService {

    TransactionResponseDto transfer(TransferRequestDto request);
}
