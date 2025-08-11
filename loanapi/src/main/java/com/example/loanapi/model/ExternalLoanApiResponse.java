package com.example.loanapi.model;

import lombok.Data;
import java.util.List;

@Data
public class ExternalLoanApiResponse {
    private String loanAccountNumber;
    private List<EMIDetail> emiDetails;

    @Data
    public static class EMIDetail {
        private String month;
        private Double emiAmount;
        private Boolean paidStatus;
        private Boolean dueStatus;
    }
}
