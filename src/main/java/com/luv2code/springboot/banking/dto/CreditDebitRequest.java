package com.luv2code.springboot.banking.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreditDebitRequest {
    private String accountNumber;
    private BigDecimal amount;
}
