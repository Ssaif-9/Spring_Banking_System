package com.luv2code.springboot.banking.controller;

import com.luv2code.springboot.banking.dto.*;
import com.luv2code.springboot.banking.servicr.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/")
@Tag(name = "User Account Management APIs")
public class UserRestController {

    private final UserService userService;

    @Operation(
            summary = "Create New User Account ",
            description = "Creating a new user and assigning an account ID"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 CREATED"
    )
    @PostMapping()
    public BankResponse addNewUserAccount(@RequestBody UserRequest userRequest) {
        return userService.createAccount(userRequest);
    }

    @Operation(
            summary = "Balance Enquiry",
            description = "Given an account number ,check how much the user has"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @GetMapping("balanceEnquiry")
    public BankResponse getEnquiryAccounts(@ModelAttribute EnquiryRequest enquiryRequest) {
        return userService.balanceEnquiry(enquiryRequest);
    }

    @GetMapping("nameEnquiry")
    public String getEnquiryName(@ModelAttribute EnquiryRequest enquiryRequest) {
        return userService.nameEnquiry(enquiryRequest);
    }

    @PostMapping("credit")
    public BankResponse creditUser(@RequestBody CreditDebitRequest creditDebitRequest) {
        return userService.creditAmount(creditDebitRequest);
    }

    @PostMapping("debit")
    public BankResponse debitUser(@RequestBody CreditDebitRequest creditDebitRequest) {
        return userService.debitAmount(creditDebitRequest);
    }

    @PostMapping("transfer")
    public BankResponse transferUser(@RequestBody TransferRequest transferRequest) {
        return userService.transfer(transferRequest);
    }
}
