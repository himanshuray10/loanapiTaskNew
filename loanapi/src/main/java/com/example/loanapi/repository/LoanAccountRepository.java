package com.example.loanapi.repository;

import com.example.loanapi.model.LoanAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoanAccountRepository extends JpaRepository<LoanAccountEntity, Long> {
    Optional<LoanAccountEntity> findByLoanAccountNumber(String loanAccountNumber);
}
