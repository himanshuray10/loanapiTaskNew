package com.example.loanapi.integration;

import com.example.loanapi.model.ExternalLoanApiResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LoanApiIntegrationService {

    private final RestTemplate restTemplate = new RestTemplate();

    public ExternalLoanApiResponse fetchLoanAccount(String loanAccountNumber) {
        String url = "https://demo9993930.mockable.io/loanaccount/" + loanAccountNumber;
        return restTemplate.getForObject(url, ExternalLoanApiResponse.class);
    }
}
