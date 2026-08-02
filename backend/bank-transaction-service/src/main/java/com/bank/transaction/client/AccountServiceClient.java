package com.bank.transaction.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.bank.transaction.dto.request.UpdateBalanceRequest;
import com.bank.transaction.dto.response.AccountResponse;
import com.bank.transaction.dto.response.ApiResponse;

@Component
public class AccountServiceClient {

    private final WebClient webClient;

    public AccountServiceClient(
            WebClient.Builder webClientBuilder,
            @Value("${account.service.url}") String accountServiceUrl) {

        this.webClient = webClientBuilder
                .baseUrl(accountServiceUrl)
                .build();
    }


    public AccountResponse getAccountById(Integer accountId) {

        ApiResponse<AccountResponse> response =
                webClient.get()
                .uri("/api/accounts/{id}", accountId)
                .retrieve()
                .bodyToMono(
                    new ParameterizedTypeReference<ApiResponse<AccountResponse>>() {}
                )
                .block();


        if(response == null || response.getData() == null) {
            return null;
        }

        return response.getData();
    }

    public void updateBalance(Integer accountId,
                              UpdateBalanceRequest request) {

        webClient.put()
                .uri("/api/accounts/{id}", accountId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}