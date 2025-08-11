package com.example.loanapi.controller;

import com.example.loanapi.model.LoanAccountResponse;
import com.example.loanapi.service.LoanAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loanaccount")
@RequiredArgsConstructor
public class LoanAccountController {

    private final LoanAccountService service;

    @GetMapping("/{loanAccountNumber}")
    public LoanAccountResponse getLoanAccount(@PathVariable String loanAccountNumber) {
        return service.getLoanAccount(loanAccountNumber);
    }
}
