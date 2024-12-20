package com.luv2code.springboot.banking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AccountInfo {
    @Schema(name = "User Account Name")
    private String accountName;
    @Schema(name = "User Account Balance")
    private BigDecimal accountBalance;
    @Schema(name = "User Account Number")
    private String accountNumber;
}
