package com.luv2code.springboot.banking.servicr;

import com.luv2code.springboot.banking.dto.*;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    BankResponse createAccount(UserRequest userRequest);

    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);

    String nameEnquiry(EnquiryRequest enquiryRequest);

    BankResponse creditAmount(CreditDebitRequest creditDebitRequest);

    BankResponse debitAmount(CreditDebitRequest creditDebitRequest);

    BankResponse transfer(TransferRequest transferRequest);
}
