package com.bank.transactions.dto;

import com.bank.transactions.enums.TransactionStatus;

/**
 * Response payload returned by POST /api/transactions/transfer.
 * Shape matches the project specification exactly:
 * { "transactionId": ..., "status": ..., "message": ..., "riskScore": ... }
 */
public class TransactionResponseDto {

    private Integer transactionId;
    private TransactionStatus status;
    private String message;
//    private Integer riskScore;

    public TransactionResponseDto() {
    }

    public TransactionResponseDto(Integer transactionId, TransactionStatus status, String message) {
        this.transactionId = transactionId;
        this.status = status;
        this.message = message;
//        this.riskScore = riskScore;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

//    public Integer getRiskScore() {
//        return riskScore;
//    }
//
//    public void setRiskScore(Integer riskScore) {
//        this.riskScore = riskScore;
//    }
}
