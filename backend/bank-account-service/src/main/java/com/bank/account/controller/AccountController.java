package com.bank.account.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.account.dto.request.CreateAccountRequest;
import com.bank.account.dto.response.AccountResponse;
import com.bank.account.dto.response.ApiResponse;
import com.bank.account.service.AccountService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

	@Autowired
	private AccountService accountService;
	
	@PostMapping
	public ResponseEntity<ApiResponse<AccountResponse>> createAccount(
			@Valid @RequestBody CreateAccountRequest request){
		return ResponseEntity.ok(ApiResponse.success(accountService.createAccount(request), "Account created Successfully"));
	}
	
	@GetMapping("/{accountId}")
	public ResponseEntity<ApiResponse<AccountResponse>>getAccount(@PathVariable Integer accountId){
		return ResponseEntity.ok(ApiResponse.success(
				accountService.getAccount(accountId), "Account fetched successfully"));
	}
	
}
