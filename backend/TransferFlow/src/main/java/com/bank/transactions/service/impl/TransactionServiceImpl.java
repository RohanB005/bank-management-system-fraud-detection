package com.bank.transactions.service.impl;
import com.bank.transactions.dto.FraudCheckRequestDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.transactions.client.AccountClient;
import com.bank.transactions.dto.AccountDto;
import com.bank.transactions.dto.FraudCheckRequestDto;
import com.bank.transactions.dto.FraudResponseDto;
import com.bank.transactions.dto.TransactionResponseDto;
import com.bank.transactions.dto.TransferRequestDto;
import com.bank.transactions.entity.Transaction;
import com.bank.transactions.enums.TransactionStatus;
import com.bank.transactions.enums.TransactionType;
import com.bank.transactions.exception.InsufficientBalanceException;
import com.bank.transactions.exception.TransactionException;
import com.bank.transactions.fraud.FraudCheckService;
import com.bank.transactions.repository.TransactionRepository;
import com.bank.transactions.service.TransactionService;
import com.bank.transactions.util.ReferenceGenerator;

/**
 * Core implementation of the Transfer Flow (B2).
 *
 * Orchestration order:
 *   1. Fetch sender account via AccountClient.
 *   2. Fetch receiver account via AccountClient.
 *   3. Validate: sender != receiver, both ACTIVE, sender has sufficient balance.
 *   4. Call FraudCheckService.checkTransaction(amount).
 *   5. If FLAGGED  -> return immediately; no balances updated, no transaction saved.
 *   6. If ALLOW    -> call AccountClient.transfer(...) so the Account Service performs
 *                     the actual balance movement (this service never touches
 *                     the Account table itself).
 *   7. On success  -> persist a Transaction record with a generated reference number
 *                     and return a SUCCESS response.
 */
@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountClient accountClient;
    private final FraudCheckService fraudCheckService;

    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                   AccountClient accountClient,
                                   FraudCheckService fraudCheckService) {
        this.transactionRepository = transactionRepository;
        this.accountClient = accountClient;
        this.fraudCheckService = fraudCheckService;
    }

    @Override
    @Transactional
    public TransactionResponseDto transfer(TransferRequestDto request) {

        validateNotSameAccount(request);

        // Step 4 & 5: fetch sender and receiver via the Account Service
        AccountDto sender = accountClient.getAccountById(request.getFromAccountId());
        AccountDto receiver = accountClient.getAccountById(request.getToAccountId());

        // Step 6: validate business rules before ever calling the fraud engine
        validateAccountsActive(sender, receiver);
        validateSufficientBalance(sender, request.getAmount());

        // Step 7 & 8: delegate the fraud rule entirely to FraudCheckService.
        // This class must never contain fraud logic itself.
       
        FraudCheckRequestDto fraudRequest = new FraudCheckRequestDto();
        
        fraudRequest.setCurrentTransactionCity(request.getTransactionCity());
        fraudRequest.setCustomerId(sender.getCustomerId());
        fraudRequest.setAccountId(sender.getAccountId());
        fraudRequest.setTransactionAmount(request.getAmount());
        fraudRequest.setTransactionType("TRANSFER");
        fraudRequest.setClientIpAddress("127.0.0.1");
        fraudRequest.setCurrentTransactionCity("Pune");

        FraudResponseDto fraudResult = fraudCheckService.checkTransaction(fraudRequest);

        if (fraudResult.isFraud()) {
            return buildFlaggedResponse(fraudResult);
        }

        return executeAllowedTransfer(
                request,
                fraudResult,
                fraudRequest.getCurrentTransactionCity()
        );
    }

    private void validateNotSameAccount(TransferRequestDto request) {
        if (request.getFromAccountId().equals(request.getToAccountId())) {
            throw new TransactionException("Sender and receiver account cannot be the same");
        }
    }

    private void validateAccountsActive(AccountDto sender, AccountDto receiver) {
        if (!sender.isActive()) {
            throw new TransactionException("Sender account " + sender.getAccountId() + " is not ACTIVE");
        }
        if (!receiver.isActive()) {
            throw new TransactionException("Receiver account " + receiver.getAccountId() + " is not ACTIVE");
        }
    }

    private void validateSufficientBalance(AccountDto sender, BigDecimal amount) {
        if (sender.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException(sender.getAccountId(), sender.getBalance(), amount);
        }
    }

    /**
     * Step 9: a FLAGGED result stops the flow entirely — no balance update,
     * no Transaction record is persisted. The caller is told immediately.
     */
    private TransactionResponseDto buildFlaggedResponse(FraudResponseDto fraudResult) {
    	return new TransactionResponseDto(
    	        null,
    	        TransactionStatus.FLAGGED,
    	        fraudResult.getMessage()
    	);
    }

    /**
     * Steps 10 & 11: the Account Service performs the actual balance movement;
     * on success this service records its own Transaction entry.
     */
    private TransactionResponseDto executeAllowedTransfer(
            TransferRequestDto request,
            FraudResponseDto fraudResult,
            String transactionCity) {
        AccountClient.TransferResult transferResult = accountClient.transfer(request);

        if (!"SUCCESS".equalsIgnoreCase(transferResult.getStatus())) {
            throw new TransactionException("Account Service did not confirm the transfer as successful");
        }

        String referenceNumber = ReferenceGenerator.generate();

        Transaction transaction = new Transaction(
                request.getFromAccountId(),
                TransactionType.TRANSFER,
                request.getAmount(),
                transferResult.getFromAccountBalance(),
                "Transfer to account " + request.getToAccountId(),
                LocalDateTime.now(),
                transactionCity,
                //null, // transactionCity — not part of the current request payload
                referenceNumber,
                TransactionStatus.SUCCESS
        );

        Transaction saved = transactionRepository.save(transaction);

        return new TransactionResponseDto(
                saved.getTransactionId(),
                TransactionStatus.SUCCESS,
                "Transfer Successful"
        );
    }
}
