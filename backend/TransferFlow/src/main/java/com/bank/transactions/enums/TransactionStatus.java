package com.bank.transactions.enums;

/**
 * Represents the final outcome status of a transaction.
 *
 * SUCCESS - transfer completed and balances updated by the Account Service.
 * FAILED  - transfer attempted but the Account Service reported a failure.
 * FLAGGED - transfer was stopped by the Fraud Check Service before execution.
 * BLOCKED - transfer was blocked due to a validation failure (e.g. inactive account).
 */
public enum TransactionStatus {
    SUCCESS,
    FAILED,
    FLAGGED,
    BLOCKED
}
