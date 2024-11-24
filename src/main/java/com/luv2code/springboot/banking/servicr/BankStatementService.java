package com.luv2code.springboot.banking.servicr;

import com.luv2code.springboot.banking.dto.StatementRequest;
import com.luv2code.springboot.banking.entity.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BankStatementService {

    List<Transaction> generateStatement(StatementRequest statementRequest);
}
