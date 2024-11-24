package com.luv2code.springboot.banking.servicr;

import com.luv2code.springboot.banking.dto.*;
import com.luv2code.springboot.banking.entity.User;
import com.luv2code.springboot.banking.reposatiry.UserRepo;
import com.luv2code.springboot.banking.utilitize.AccountUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepo userRepo;
    private final EmailService emailService;
    private final TransactionService transactionService;

    @Override
    public BankResponse createAccount(UserRequest userRequest) {
        if(userRepo.existsByEmail(userRequest.getEmail())){
            return BankResponse.builder()
                    .responseCode(AccountUtil.Account_Exists_code)
                    .responseMessage(AccountUtil.Account_Exists_Message)
                    .accountInfo(null)
                    .build();
        }
        User newUser= User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(AccountUtil.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .email(userRequest.getEmail())
                .phoneNumber(userRequest.getPhoneNumber())
                .alternatePhoneNumber(userRequest.getAlternatePhoneNumber())
                .status("Active")
                .build();

        userRepo.save(newUser);

        //Send email Alert
        EmailDetail emailDetails = EmailDetail.builder()
                .recipient(newUser.getEmail())
                .messageSubject("ACCOUNT CREATION")
                .messageBody("Congratulations! Your Account Has been Successfully Created.\nYour Account Details: \n" +
                        "Account Name: " + newUser.getFirstName() + " " + newUser.getLastName() + " " + newUser.getOtherName() + "\nAccount Number: " + newUser.getAccountNumber())
                .build();
        emailService.sendEmailAlert(emailDetails);

        return BankResponse.builder()
                .responseCode(AccountUtil.Account_Created_code)
                .responseMessage(AccountUtil.Account_Created_Message)
                .accountInfo(AccountInfo.builder()
                        .accountName(newUser.getFirstName())
                        .accountBalance(newUser.getAccountBalance())
                        .accountNumber(newUser.getAccountNumber())
                        .build())
                .build();
    }

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {
        if(!userRepo.existsByAccountNumber(enquiryRequest.getAccountNumber())){
            return BankResponse.builder()
                    .responseCode(AccountUtil.Account_Not_Exists_code)
                    .responseMessage(AccountUtil.Account_Not_Exists_Message)
                    .accountInfo(null)
                    .build();
        }
        User enquiryUser =userRepo.findByAccountNumber(enquiryRequest.getAccountNumber());

        return BankResponse.builder()
                .responseCode(AccountUtil.Account_Found_code)
                .responseMessage(AccountUtil.Account_Found_Message)
                .accountInfo(AccountInfo.builder()
                        .accountName(enquiryUser.getFirstName())
                        .accountBalance(enquiryUser.getAccountBalance())
                        .accountNumber(enquiryRequest.getAccountNumber())
                        .build())
                .build();

    }

    @Override
    public String nameEnquiry(EnquiryRequest enquiryRequest) {
        if(!userRepo.existsByAccountNumber(enquiryRequest.getAccountNumber())){
            return AccountUtil.Account_Not_Exists_Message;
        }

        User enquiryUser = userRepo.findByAccountNumber(enquiryRequest.getAccountNumber());
        return enquiryUser.getFirstName()+" "+enquiryUser.getLastName()+" "+enquiryUser.getOtherName()+".";
    }

    @Override
    public BankResponse creditAmount(CreditDebitRequest creditDebitRequest) {
        if(!userRepo.existsByAccountNumber(creditDebitRequest.getAccountNumber())){
            return BankResponse.builder()
                    .responseCode(AccountUtil.Account_Not_Exists_code)
                    .responseMessage(AccountUtil.Account_Not_Exists_Message)
                    .accountInfo(null)
                    .build();
        }
        User enquiryUser = userRepo.findByAccountNumber(creditDebitRequest.getAccountNumber());
        enquiryUser.setAccountBalance(enquiryUser.getAccountBalance().add(creditDebitRequest.getAmount()));
        userRepo.save(enquiryUser);

        EmailDetail emailDetail = EmailDetail.builder()
                .recipient(enquiryUser.getEmail())
                .messageSubject("Adding Money...")
                .messageBody(enquiryUser.getFirstName() + " " + enquiryUser.getLastName()+"\nAccount Number : "+enquiryUser.getAccountNumber()+"\nNew Balance : "+enquiryUser.getAccountBalance())
                .build();
        emailService.sendEmailAlert(emailDetail);

        TransactionDto creditTransactionDto =TransactionDto.builder()
                .accountNumber(enquiryUser.getAccountNumber())
                .transactionType("Credit")
                .amount(creditDebitRequest.getAmount())
                .build();
        transactionService.saveTransaction(creditTransactionDto);

        return BankResponse.builder()
                .responseCode(AccountUtil.Amount_Credited_code)
                .responseMessage(AccountUtil.Amount_Credited_Message)
                .accountInfo(AccountInfo.builder()
                        .accountName(enquiryUser.getFirstName())
                        .accountBalance(enquiryUser.getAccountBalance())
                        .accountNumber(enquiryUser.getAccountNumber())
                        .build())
                .build();
    }

    @Override
    public BankResponse debitAmount(CreditDebitRequest creditDebitRequest) {
        if(!userRepo.existsByAccountNumber(creditDebitRequest.getAccountNumber())){
            return BankResponse.builder()
                    .responseCode(AccountUtil.Account_Not_Exists_code)
                    .responseMessage(AccountUtil.Account_Not_Exists_Message)
                    .accountInfo(null)
                    .build();
        }

        User enquiryUser = userRepo.findByAccountNumber(creditDebitRequest.getAccountNumber());

        BigInteger accountBalance = enquiryUser.getAccountBalance().toBigInteger();
        BigInteger amountDebit = creditDebitRequest.getAmount().toBigInteger();
        if(accountBalance.intValue()<amountDebit.intValue()){
            return BankResponse.builder()
                    .responseCode(AccountUtil.Balance_Not_Enough_code)
                    .responseMessage(AccountUtil.Balance_Not_Enough_Message)
                    .accountInfo(AccountInfo.builder()
                            .accountName(enquiryUser.getFirstName())
                            .accountBalance(enquiryUser.getAccountBalance())
                            .accountNumber(enquiryUser.getAccountNumber())
                            .build())
                    .build();
        }
        else{
            enquiryUser.setAccountBalance(enquiryUser.getAccountBalance().subtract(creditDebitRequest.getAmount()));
            userRepo.save(enquiryUser);

            EmailDetail emailDetail =EmailDetail.builder()
                    .recipient(enquiryUser.getEmail())
                    .messageSubject("Debit Money...")
                    .messageBody(enquiryUser.getFirstName() + " " + enquiryUser.getLastName()+"\nAccount Number : "+enquiryUser.getAccountNumber()+"\nNew Balance : "+enquiryUser.getAccountBalance())
                    .build();

            emailService.sendEmailAlert(emailDetail);

            TransactionDto debitTransactionDto =TransactionDto.builder()
                    .accountNumber(enquiryUser.getAccountNumber())
                    .transactionType("Debit")
                    .amount(creditDebitRequest.getAmount())
                    .build();
            transactionService.saveTransaction(debitTransactionDto);

            return BankResponse.builder()
                    .responseCode(AccountUtil.Amount_Debited_code)
                    .responseMessage(AccountUtil.Amount_Debited_Message)
                    .accountInfo(AccountInfo.builder()
                            .accountName(enquiryUser.getFirstName())
                            .accountBalance(enquiryUser.getAccountBalance())
                            .accountNumber(enquiryUser.getAccountNumber())
                            .build())
                    .build();
        }
    }

    @Override
    public BankResponse transfer(TransferRequest transferRequest) {
        Boolean isSourceAccountExist=userRepo.existsByAccountNumber(transferRequest.getSourceAccountNumber());
        Boolean isDestinationAccountExist=userRepo.existsByAccountNumber(transferRequest.getDestinationAccountNumber());

        if(!isDestinationAccountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtil.Transfer_Destination_Not_Found_code)
                    .responseMessage(AccountUtil.Transfer_Destination_Not_Found_Message)
                    .accountInfo(null)
                    .build();
        }

        if(!isSourceAccountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtil.Transfer_Source_Not_Found_code)
                    .responseMessage(AccountUtil.Transfer_Source_Not_Found_Message)
                    .accountInfo(null)
                    .build();
        }

        User sourceUser =userRepo.findByAccountNumber(transferRequest.getSourceAccountNumber());

        if(transferRequest.getAmount().compareTo(sourceUser.getAccountBalance()) > 0){ //(1) if balance less than amount - (0) if balance equal amount - (-1) if balance not less than amount
            return BankResponse.builder()
                    .responseCode(AccountUtil.Balance_Not_Enough_code)
                    .responseMessage(AccountUtil.Balance_Not_Enough_Message)
                    .accountInfo(null)
                    .build();

        }

        sourceUser.setAccountBalance(sourceUser.getAccountBalance().subtract(transferRequest.getAmount()));
        userRepo.save(sourceUser);
        EmailDetail sourceEmailDetail=EmailDetail.builder()
                .recipient(sourceUser.getEmail())
                .messageSubject("Debit Money...")
                .messageBody("Debit amount ("+transferRequest.getAmount().toString()
                            +") from your account with number ("+transferRequest.getSourceAccountNumber()
                            +")to account with number ("+transferRequest.getDestinationAccountNumber()+") .")
                .build();
        emailService.sendEmailAlert(sourceEmailDetail);

        TransactionDto sourceTransactionDto =TransactionDto.builder()
                .accountNumber(sourceUser.getAccountNumber())
                .transactionType("Debit")
                .amount(transferRequest.getAmount())
                .build();
        transactionService.saveTransaction(sourceTransactionDto);


        User destinationUser = userRepo.findByAccountNumber(transferRequest.getDestinationAccountNumber());
        destinationUser.setAccountBalance(destinationUser.getAccountBalance().add(transferRequest.getAmount()));
        userRepo.save(destinationUser);


        EmailDetail destinationesEmailDetail=EmailDetail.builder()
                .recipient(destinationUser.getEmail())
                .messageSubject("Credit Money...")
                .messageBody("Credit amount ("+transferRequest.getAmount().toString()
                        +") from account with number ("+transferRequest.getDestinationAccountNumber()
                        +")to your account with number ("+transferRequest.getSourceAccountNumber()+") .")
                .build();
        emailService.sendEmailAlert(destinationesEmailDetail);

        TransactionDto destinationesTransactionDto =TransactionDto.builder()
                .accountNumber(destinationUser.getAccountNumber())
                .transactionType("Credit")
                .amount(transferRequest.getAmount())
                .build();
        transactionService.saveTransaction(destinationesTransactionDto);


        return BankResponse.builder()
                .responseCode(AccountUtil.Transfer_Successfully_code)
                .responseMessage(AccountUtil.Transfer_Successfully_Message)
                .accountInfo(null)
                .build();
    }
}
