package com.bank.transaction.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionResponse {

    private Integer transactionId;

    private String referenceNumber;

    private String transactionType;
    
    private String description;

    private String transactionCity;

    private BigDecimal amount;

    private BigDecimal availableBalance;

    private String status;

    private LocalDateTime transactionTime;

    private String message;

    // Default Constructor
    public TransactionResponse() {
    }

    public TransactionResponse(
            Integer transactionId,
            String referenceNumber,
            String transactionType,
            BigDecimal amount,
            BigDecimal availableBalance,
            String description,
            String transactionCity,
            String status,
            LocalDateTime transactionTime,
            String message) {

        this.transactionId = transactionId;
        this.referenceNumber = referenceNumber;
        this.transactionType = transactionType;
        this.amount = amount;
        this.availableBalance = availableBalance;
        this.description = description;
        this.transactionCity = transactionCity;
        this.status = status;
        this.transactionTime = transactionTime;
        this.message = message;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(LocalDateTime transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTransactionCity() {
        return transactionCity;
    }

    public void setTransactionCity(String transactionCity) {
        this.transactionCity = transactionCity;
    }
}