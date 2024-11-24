package com.luv2code.springboot.banking.servicr;

import com.luv2code.springboot.banking.dto.TransactionDto;
import com.luv2code.springboot.banking.entity.Transaction;
import com.luv2code.springboot.banking.reposatiry.TransactionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionServiceImp implements TransactionService {

    private final TransactionRepo transactionRepo;


    @Override
    public void saveTransaction(TransactionDto transactionDto) {
        Transaction transaction=Transaction.builder()
                .transactionType(transactionDto.getTransactionType())
                .accountNumber(transactionDto.getAccountNumber())
                .amount(transactionDto.getAmount())
                .status("SUCCESS")
                .build();

        transactionRepo.save(transaction);
        System.out.println("Transaction saved Successfully");
    }
}
