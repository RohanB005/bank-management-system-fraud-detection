package com.bank.account.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.account.client.AuthServiceClient;
import com.bank.account.dto.request.CreateAccountRequest;
import com.bank.account.dto.request.UpdateAccountRequest;
import com.bank.account.dto.response.AccountResponse;
import com.bank.account.entity.Account;
import com.bank.account.exception.InvalidOperationException;
import com.bank.account.exception.ResourceNotFoundException;
import com.bank.account.repository.AccountRepository;
import com.bank.account.service.AccountService;

import jakarta.validation.Valid;
@Service
public class AccountServiceImpl implements AccountService{

	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private AuthServiceClient authServiceClient;

	@Override
	public AccountResponse createAccount(@Valid CreateAccountRequest request) {
		boolean exists = authServiceClient.customerExists(request.getCustomerId());
		if(!exists) {
			throw new ResourceNotFoundException(
					"Customer not found with id: " + request.getCustomerId());
		}
		
		Account account =  new Account();
		account.setCustomerId(request.getCustomerId());
		account.setAccountNumber(generateAccountNumber());
		account.setAccountType(request.getAccountType());
		account.setBranchName(request.getBranchName());
		account.setIfscCode(request.getIfscCode());
		account.setBalance(BigDecimal.ZERO);
		account.setStatus(Account.AccountStatus.Active);
		
		Account saved = accountRepository.save(account);
		return AccountResponse.fromEntity(saved);
		
		
	}

	private String generateAccountNumber() {
		String number;
		do {
			number = String.valueOf(100000000000L + (long) (new Random().nextDouble() * 900000000000L));
		} while (accountRepository.existsByAccountNumber(number));
		return number;
	}

	@Override
	public AccountResponse getAccount(Integer accountId) {
		return AccountResponse.fromEntity(findAccountOrThrow(accountId));
		
	}

	private Account findAccountOrThrow(Integer accountId) {
		return accountRepository.findById(accountId).orElseThrow(() -> new ResourceNotFoundException(
				"Account not found with id: " + accountId));
	}

	@Override
	public List<AccountResponse> getAccountByCustomer(Integer customerId) {
		return accountRepository.findByCustomerId(customerId).stream()
				.map(AccountResponse::fromEntity)
				.collect(Collectors.toList());
	}

	@Override
	public AccountResponse updateAccount(Integer accountId, UpdateAccountRequest request) {
		Account account = findAccountOrThrow(accountId);
		if(request.getAccountType() != null) {
			account.setAccountType(request.getAccountType());
		}
		return AccountResponse.fromEntity(accountRepository.save(account));
	}

	@Override
	public AccountResponse closeAccount(Integer accountId) {
		Account account = findAccountOrThrow(accountId);
		if(account.getBalance().compareTo(BigDecimal.ZERO) !=0) {
			throw new InvalidOperationException(
					"Cannot close account with a non-zero balance. Current balance: " + account.getBalance());
		}
		account.setStatus(Account.AccountStatus.Closed);
		return AccountResponse.fromEntity(accountRepository.save(account));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
