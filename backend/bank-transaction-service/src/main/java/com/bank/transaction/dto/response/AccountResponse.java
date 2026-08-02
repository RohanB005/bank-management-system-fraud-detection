package com.bank.transaction.dto.response;

import java.math.BigDecimal;

public class AccountResponse {

    private Integer accountId;
    private Integer customerId;
    private String accountNumber;
    private String accountType;
    private BigDecimal balance;
    private String branchName;
    private String ifscCode;
    private String status;


    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }


    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}