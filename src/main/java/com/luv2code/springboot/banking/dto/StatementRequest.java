package com.luv2code.springboot.banking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatementRequest {
    private String accountNumber;
    private LocalDate startDate;
    private LocalDate endDate;
}
