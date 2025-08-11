package com.example.loanapi.integration;

import com.example.loanapi.model.ExternalLoanApiResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class LoanApiIntegrationService {

    private static final Logger logger = LoggerFactory.getLogger(LoanApiIntegrationService.class);

    private final WebClient externalLoanWebClient;

    public ExternalLoanApiResponse fetchLoanAccount(String loanAccountNumber) {
        try {
            return externalLoanWebClient
                    .get()
                    .uri("/loanaccount/{loanAccountNumber}", loanAccountNumber)
                    .retrieve()
                    .bodyToMono(ExternalLoanApiResponse.class)
                    .onErrorResume(WebClientResponseException.class, ex -> {
                        logger.error("External API error: status={} body={}", ex.getStatusCode(), ex.getResponseBodyAsString());
                        return Mono.empty();
                    })
                    .onErrorResume(Throwable.class, ex -> {
                        logger.error("Failed to call external API", ex);
                        return Mono.empty();
                    })
                    .block();
        } catch (Exception ex) {
            logger.error("Unexpected error calling external API", ex);
            return null;
        }
    }
}
