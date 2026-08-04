package com.bank.transactions.fraud;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bank.transactions.dto.FraudCheckRequestDto;
import com.bank.transactions.dto.FraudResponseDto;

@Service
public class StubFraudCheckService implements FraudCheckService {

    private static final String FRAUD_API_URL =
            "https://26.93.90.128:59916/api/Fraud/check";

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public FraudResponseDto checkTransaction(FraudCheckRequestDto request) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<FraudCheckRequestDto> entity =
                new HttpEntity<>(request, headers);

        return restTemplate.postForObject(
        		FRAUD_API_URL,
                entity,
                FraudResponseDto.class
        );
    }
}