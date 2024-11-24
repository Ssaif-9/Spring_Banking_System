package com.luv2code.springboot.banking.controller;

import com.itextpdf.text.DocumentException;
import com.luv2code.springboot.banking.dto.StatementRequest;
import com.luv2code.springboot.banking.entity.Transaction;
import com.luv2code.springboot.banking.servicr.BankStatementService;
import com.luv2code.springboot.banking.servicr.BankStatementServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bankStatement")
public class BankStatementRestController {

    private final BankStatementService bankStatementService;

    @GetMapping
    public List<Transaction> findStatement (@ModelAttribute StatementRequest statementRequest) throws FileNotFoundException, DocumentException {
        return bankStatementService.generateStatement(statementRequest);

    }
}
