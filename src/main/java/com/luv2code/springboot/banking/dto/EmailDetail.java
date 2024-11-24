package com.luv2code.springboot.banking.dto;

import lombok.*;
import org.springframework.stereotype.Service;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EmailDetail {
    private String recipient;
    private String messageSubject;
    private String messageBody;
    private String messageAttachment;
}
