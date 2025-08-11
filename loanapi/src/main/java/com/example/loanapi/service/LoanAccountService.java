package com.example.loanapi.service;

import com.example.loanapi.integration.LoanApiIntegrationService;
import com.example.loanapi.model.ExternalLoanApiResponse;
import com.example.loanapi.model.LoanAccountEntity;
import com.example.loanapi.model.LoanAccountResponse;
import com.example.loanapi.repository.LoanAccountRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoanAccountService {

    private static final Logger logger = LoggerFactory.getLogger(LoanAccountService.class);

    private final LoanApiIntegrationService integrationService;
    private final LoanAccountRepository repository;

    public LoanAccountResponse getLoanAccount(String loanAccountNumber) {
        logger.info("Fetching loan account details for loanAccountNumber: {}", loanAccountNumber);

        Optional<LoanAccountEntity> existingEntity = repository.findByLoanAccountNumber(loanAccountNumber);
        if (existingEntity.isPresent()) {
            logger.info("Found loan account in database, returning it.");
            return mapEntityToResponse(existingEntity.get());
        }

        ExternalLoanApiResponse externalResponse = integrationService.fetchLoanAccount(loanAccountNumber);
        if (externalResponse == null) {
            logger.error("External API returned null for loanAccountNumber: {}", loanAccountNumber);
            return null;
        }

        LocalDate dueDate = null;
        Double emiAmount = null;

        if (externalResponse.getEmiDetails() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
            for (ExternalLoanApiResponse.EMIDetail emiDetail : externalResponse.getEmiDetails()) {
                Boolean isDue = emiDetail.getDueStatus();
                if (Boolean.TRUE.equals(isDue)) {
                    dueDate = LocalDate.parse(emiDetail.getMonth(), formatter).withDayOfMonth(1);
                    emiAmount = emiDetail.getEmiAmount();
                    break;
                }
            }
        }

        if (dueDate == null || emiAmount == null) {
            logger.warn("No EMI with due status found for loanAccountNumber: {}", loanAccountNumber);
            dueDate = LocalDate.now().plusMonths(1);
            emiAmount = 0.0;
        }

        LoanAccountEntity entity = new LoanAccountEntity();
        entity.setLoanAccountNumber(externalResponse.getLoanAccountNumber());
        entity.setDueDate(dueDate);
        entity.setEmiAmount(emiAmount);

        repository.save(entity);
        logger.info("Saved loan account to database: {}", entity);

        return mapEntityToResponse(entity);
    }

    private LoanAccountResponse mapEntityToResponse(LoanAccountEntity entity) {
        LoanAccountResponse response = new LoanAccountResponse();
        response.setLoanAccountNumber(entity.getLoanAccountNumber());
        response.setDueDate(entity.getDueDate());
        response.setEmiAmount(entity.getEmiAmount());
        return response;
    }
}
