package com.example.loanapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanAccountResponse {
    private String loanAccountNumber;
    private LocalDate dueDate;
    private Double emiAmount;
}
