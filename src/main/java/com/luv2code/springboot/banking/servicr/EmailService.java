package com.luv2code.springboot.banking.servicr;

import com.luv2code.springboot.banking.dto.EmailDetail;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {

    void sendEmailAlert(EmailDetail emailDetail);
    void sendEmailWithAttachment(EmailDetail emailDetail);
}
