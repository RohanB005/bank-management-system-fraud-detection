package com.bank.transactions.dto;

/**
 * Result returned by any FraudCheckService implementation.
 * status is expected to be "ALLOW" or "FLAGGED" per the current stub rule;
 * a real fraud engine may extend this vocabulary later without changing the
 * shape of this class.
 */
public class FraudResponseDto {

    private String status;
    private int riskScore;

    public FraudResponseDto() {
    }

    public FraudResponseDto(String status, int riskScore) {
        this.status = status;
        this.riskScore = riskScore;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(int riskScore) {
        this.riskScore = riskScore;
    }

    /** True when the fraud engine flagged this transaction as suspicious. */
    public boolean isFlagged() {
        return "FLAGGED".equalsIgnoreCase(status);
    }
}
