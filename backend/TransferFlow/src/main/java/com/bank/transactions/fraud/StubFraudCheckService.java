package com.bank.transactions.fraud;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.bank.transactions.dto.FraudResponseDto;

/**
 * Temporary stand-in for the real .NET Fraud Detection API.
 *
 * Rule: amount > 50,000  -> FLAGGED, riskScore = 90
 *       otherwise        -> ALLOW,   riskScore = 10
 *
 * This lets the Transfer Flow be built and tested end-to-end without waiting
 * on the real fraud engine. Because TransactionServiceImpl depends only on
 * the FraudCheckService interface, replacing this class later requires no
 * change anywhere else in the Transaction Service.
 */
@Service
public class StubFraudCheckService implements FraudCheckService {

    private static final BigDecimal FLAG_THRESHOLD = new BigDecimal("50000");
    private static final int FLAGGED_RISK_SCORE = 90;
    private static final int ALLOWED_RISK_SCORE = 10;

    @Override
    public FraudResponseDto checkTransaction(BigDecimal amount) {
        if (amount.compareTo(FLAG_THRESHOLD) > 0) {
            return new FraudResponseDto("FLAGGED", FLAGGED_RISK_SCORE);
        }
        return new FraudResponseDto("ALLOW", ALLOWED_RISK_SCORE);
    }
}
