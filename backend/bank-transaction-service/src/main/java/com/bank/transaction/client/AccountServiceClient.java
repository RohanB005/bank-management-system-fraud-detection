package com.bank.transaction.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.bank.transaction.dto.request.UpdateBalanceRequest;
import com.bank.transaction.dto.response.AccountResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccountServiceClient {
	
	@Value("${account.service.url}")
	private String accountServiceUrl;
	
	private final WebClient.Builder webClientBuilder;
	
	public AccountServiceClient(WebClient.Builder webClientBuilder) {
		this.webClientBuilder = webClientBuilder;
	}
	
	public AccountResponse getAccountById(Integer accountId) {
		
		return webClientBuilder.build().get().uri(accountServiceUrl + "/api/accounts" + accountId).retrieve().bodyToMono(AccountResponse.class).block();
	}
	
	public AccountResponse updateBalance(Integer accountId, UpdateBalanceRequest request) {
		return webClientBuilder.build().put().uri(accountServiceUrl + "/api/accounts/" + accountId + "/balance").bodyValue(request).retrieve().bodyToMono(AccountResponse.class).block();
	}

}
