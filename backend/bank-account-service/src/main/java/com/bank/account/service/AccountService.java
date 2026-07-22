package com.bank.account.service;

import java.util.List;

import com.bank.account.dto.request.CreateAccountRequest;
import com.bank.account.dto.request.UpdateAccountRequest;
import com.bank.account.dto.response.AccountResponse;

import jakarta.validation.Valid;

public interface AccountService {

	AccountResponse createAccount(@Valid CreateAccountRequest request);

	AccountResponse getAccount(Integer accountId);

	List<AccountResponse> getAccountByCustomer(Integer customerId);

	AccountResponse updateAccount(Integer accountId, UpdateAccountRequest request);

	AccountResponse closeAccount(Integer accountId);

}
