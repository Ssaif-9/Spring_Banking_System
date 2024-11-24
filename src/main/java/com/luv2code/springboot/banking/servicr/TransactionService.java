package com.luv2code.springboot.banking.servicr;

import com.luv2code.springboot.banking.dto.TransactionDto;
import org.springframework.stereotype.Service;

@Service
public interface TransactionService {
    void saveTransaction(TransactionDto transactionDto);

}
