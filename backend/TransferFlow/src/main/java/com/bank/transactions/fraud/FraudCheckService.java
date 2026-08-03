package com.bank.transactions.fraud;

import java.math.BigDecimal;

import com.bank.transactions.dto.FraudResponseDto;

/**
 * Contract for evaluating a transaction's fraud risk.
 *
 * TransactionServiceImpl depends ONLY on this interface and must never
 * contain fraud rules itself (e.g. "if amount > 50000"). Today,
 * StubFraudCheckService is the active implementation. When the real .NET
 * Fraud Detection API is ready, a new implementation of this interface can
 * be introduced and wired in without touching TransactionServiceImpl.
 */
public interface FraudCheckService {

    FraudResponseDto checkTransaction(BigDecimal amount);
}
