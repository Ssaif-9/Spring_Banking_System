package com.luv2code.springboot.banking.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EnquiryRequest {
    private String accountNumber;
}
