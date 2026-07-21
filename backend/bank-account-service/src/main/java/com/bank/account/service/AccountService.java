package com.bank.account.service;

import com.bank.account.dto.request.CreateAccountRequest;
import com.bank.account.dto.response.AccountResponse;

import jakarta.validation.Valid;

public interface AccountService {

	AccountResponse createAccount(@Valid CreateAccountRequest request);

	AccountResponse getAccount(Integer accountId);

}
