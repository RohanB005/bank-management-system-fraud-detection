package com.bank.transaction.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.bank.transaction.client.AccountServiceClient;
import com.bank.transaction.dto.request.DepositRequest;
import com.bank.transaction.dto.request.UpdateBalanceRequest;
import com.bank.transaction.dto.request.WithdrawRequest;
import com.bank.transaction.dto.response.AccountResponse;
import com.bank.transaction.dto.response.TransactionResponse;
import com.bank.transaction.entity.Transaction;
import com.bank.transaction.entity.TransactionStatus;
import com.bank.transaction.entity.TransactionType;
import com.bank.transaction.repository.TransactionRepository;
import com.bank.transaction.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    private final AccountServiceClient accountServiceClient;

    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  AccountServiceClient accountServiceClient) {
        this.transactionRepository = transactionRepository;
        this.accountServiceClient = accountServiceClient;
    }

    @Override
    public TransactionResponse deposit(DepositRequest request) {

        AccountResponse account =
                accountServiceClient.getAccountById(request.getAccountId());

        if (account == null) {
            throw new RuntimeException("Account not found");
        }

        BigDecimal updatedBalance =
                account.getBalance().add(request.getAmount());

        UpdateBalanceRequest updateRequest =
                new UpdateBalanceRequest(updatedBalance);

        accountServiceClient.updateBalance(
                request.getAccountId(),
                updateRequest);

        Transaction transaction = new Transaction();

        transaction.setAccountId(request.getAccountId());
        transaction.setTransactionType(TransactionType.Deposit);
        transaction.setAmount(request.getAmount());
        transaction.setAvailableBalance(updatedBalance);
        transaction.setDescription(request.getDescription());
        transaction.setTransactionCity(request.getTransactionCity());
        transaction.setReferenceNumber(generateReferenceNumber());
        transaction.setTransactionTime(LocalDateTime.now());
        transaction.setRiskScore(0);
        transaction.setStatus(TransactionStatus.Success);

        Transaction savedTransaction =
                transactionRepository.save(transaction);

        TransactionResponse response =
                new TransactionResponse();

        response.setTransactionId(savedTransaction.getTransactionId());
        response.setReferenceNumber(savedTransaction.getReferenceNumber());
        response.setTransactionType(
                savedTransaction.getTransactionType().name());
        response.setAmount(savedTransaction.getAmount());
        response.setAvailableBalance(
                savedTransaction.getAvailableBalance());
        response.setStatus(
                savedTransaction.getStatus().name());
        response.setTransactionTime(
                savedTransaction.getTransactionTime());
        response.setMessage("Amount deposited Successfully");

        return response;
    }

	@Override
	public TransactionResponse withdraw(WithdrawRequest request) {
		AccountResponse account =
	            accountServiceClient.getAccountById(request.getAccountId());

	    if (account == null) {
	        throw new RuntimeException("Account not found");
	    }

	    if (!"Active".equalsIgnoreCase(account.getStatus())) {
	        throw new RuntimeException("Account is not active");
	    }

	    if (account.getBalance().compareTo(request.getAmount()) < 0) {
	        throw new RuntimeException("Insufficient balance");
	    }

	    BigDecimal updatedBalance =
	            account.getBalance().subtract(request.getAmount());

	    UpdateBalanceRequest updateRequest =
	            new UpdateBalanceRequest(updatedBalance);

	    accountServiceClient.updateBalance(
	            request.getAccountId(),
	            updateRequest);

	    Transaction transaction = new Transaction();

	    transaction.setAccountId(request.getAccountId());
	    transaction.setTransactionType(TransactionType.Withdraw);
	    transaction.setAmount(request.getAmount());
	    transaction.setAvailableBalance(updatedBalance);
	    transaction.setDescription(request.getDescription());
	    transaction.setTransactionCity(request.getTransactionCity());
	    transaction.setReferenceNumber(generateReferenceNumber());
	    transaction.setTransactionTime(LocalDateTime.now());
	    transaction.setRiskScore(0);
	    transaction.setStatus(TransactionStatus.Success);

	    Transaction savedTransaction =
	            transactionRepository.save(transaction);

	    TransactionResponse response = new TransactionResponse();

	    response.setTransactionId(savedTransaction.getTransactionId());
	    response.setReferenceNumber(savedTransaction.getReferenceNumber());
	    response.setTransactionType(savedTransaction.getTransactionType().name());
	    response.setAmount(savedTransaction.getAmount());
	    response.setAvailableBalance(savedTransaction.getAvailableBalance());
	    response.setStatus(savedTransaction.getStatus().name());
	    response.setTransactionTime(savedTransaction.getTransactionTime());
	    response.setMessage("Amount withdrawn successfully");

	    return response;
	}
	

	@Override
	public TransactionResponse getTransactionById(Integer transactionId) {
		Transaction transaction = transactionRepository.findById(transactionId)
	            .orElseThrow(() ->
	                    new RuntimeException("Transaction not found"));

	    TransactionResponse response = new TransactionResponse();

	    response.setTransactionId(transaction.getTransactionId());
	    response.setReferenceNumber(transaction.getReferenceNumber());
	    response.setTransactionType(transaction.getTransactionType().name());
	    response.setAmount(transaction.getAmount());
	    response.setAvailableBalance(transaction.getAvailableBalance());
	    response.setStatus(transaction.getStatus().name());
	    response.setTransactionTime(transaction.getTransactionTime());
	    response.setMessage("Transaction fetched successfully");

	    return response;
	}

	@Override
	public List<TransactionResponse> getTransactionHistory(Integer accountId) {
		 List<Transaction> transactions =
		            transactionRepository.findByAccountIdOrderByTransactionTimeDesc(accountId);

		    return transactions.stream()
		            .map(transaction -> {

		                TransactionResponse response = new TransactionResponse();

		                response.setTransactionId(transaction.getTransactionId());
		                response.setReferenceNumber(transaction.getReferenceNumber());
		                response.setTransactionType(transaction.getTransactionType().name());
		                response.setAmount(transaction.getAmount());
		                response.setAvailableBalance(transaction.getAvailableBalance());
		                response.setStatus(transaction.getStatus().name());
		                response.setTransactionTime(transaction.getTransactionTime());
		                response.setMessage("Success");

		                return response;
		            })
		            .toList();
	}
	
	private String generateReferenceNumber() {
		return "TXN"
	            + UUID.randomUUID()
	                    .toString()
	                    .replace("-", "")
	                    .substring(0, 12)
	                    .toUpperCase();
	}
	
}
    
    