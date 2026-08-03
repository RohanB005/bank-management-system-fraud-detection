package com.bank.transaction.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
                .header("Authorization", getAuthorizationHeader())
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
        .uri("/api/accounts/{id}/balance", accountId)
        .header("Authorization", getAuthorizationHeader())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .bodyValue(request)
        .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
    
    private String getAuthorizationHeader() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return null;
        }

        return "Bearer " + authentication.getPrincipal();
    }
}