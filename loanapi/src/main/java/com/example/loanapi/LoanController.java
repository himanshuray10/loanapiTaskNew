package com.example.loanapi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoanController {

    @GetMapping("/")
    public String health() {
        return "Loan API is running";
    }
}
