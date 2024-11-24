package com.luv2code.springboot.banking.servicr;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.luv2code.springboot.banking.dto.EmailDetail;
import com.luv2code.springboot.banking.dto.StatementRequest;
import com.luv2code.springboot.banking.entity.Transaction;
import com.luv2code.springboot.banking.entity.User;
import com.luv2code.springboot.banking.reposatiry.TransactionRepo;
import com.luv2code.springboot.banking.reposatiry.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BankStatementServiceImp implements BankStatementService {

    private final TransactionRepo transactionRepo;
    private final UserRepo userRepo;
    private final EmailService emailService;

    private static final String FILE = "C:\\Users\\lenovo\\Documents\\TransactionStatement\\myStatement.pdf";

    @Override
    public List<Transaction> generateStatement(StatementRequest statementRequest) {
        LocalDate start =LocalDate.parse(statementRequest.getStartDate().toString());
        LocalDate end =LocalDate.parse(statementRequest.getEndDate().toString());

        User user = userRepo.findByAccountNumber(statementRequest.getAccountNumber());


        List<Transaction> transactions= transactionRepo.findAll().stream()
                .filter(transaction -> transaction.getAccountNumber().equals(statementRequest.getAccountNumber()))
                .filter(transaction -> transaction.getCreatedAt().equals(start))
                //.filter(transaction -> transaction.getCreatedAt().isAfter(start))
                //.filter(transaction -> transaction.getCreatedAt().isBefore(end))
                .filter(transaction -> transaction.getCreatedAt().equals(end))
                .toList();

        try {
            designStatement(transactions,statementRequest);
        } catch (FileNotFoundException | DocumentException e) {
            throw new RuntimeException(e);
        }

        EmailDetail emailDetail=EmailDetail.builder()
                .recipient(user.getEmail())
                .messageSubject("STATEMENT OF ACCOUNT ")
                .messageBody("Kindly find your requested account statement attached!")
                .messageAttachment(FILE)
                .build();

        emailService.sendEmailWithAttachment(emailDetail);

        return transactions;
    }

    private void designStatement(List<Transaction> transactions, StatementRequest statementRequest) throws FileNotFoundException, DocumentException {

        Rectangle rectangle = new Rectangle(PageSize.A4);
        Document statementDocument = new Document(rectangle);
        OutputStream outputStream = new FileOutputStream(FILE);
        PdfWriter.getInstance(statementDocument, outputStream);
        statementDocument.open();

        // Bank Information Table
        PdfPTable bankInfo = new PdfPTable(1);
        PdfPCell bankName = new PdfPCell(new Phrase("Bank of Egypt", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.WHITE)));
        bankName.setHorizontalAlignment(Element.ALIGN_CENTER);
        bankName.setBorder(Rectangle.NO_BORDER);
        bankName.setBackgroundColor(BaseColor.BLUE);
        bankName.setPadding(10f);

        PdfPCell bankAddress = new PdfPCell(new Phrase("72, Wadi El Nell, Cairo", FontFactory.getFont(FontFactory.HELVETICA, 12)));
        bankAddress.setHorizontalAlignment(Element.ALIGN_CENTER);
        bankAddress.setBorder(Rectangle.NO_BORDER);
        bankAddress.setPadding(5f);

        bankInfo.addCell(bankName);
        bankInfo.addCell(bankAddress);

        // User Information
        User statementUser = userRepo.findByAccountNumber(statementRequest.getAccountNumber());
        String fullName = statementUser.getFirstName() + " " + statementUser.getLastName() + " " + statementUser.getOtherName();
        String fullAddress = statementUser.getAddress() + " " + statementUser.getStateOfOrigin();

        PdfPTable statementInfo = new PdfPTable(2);
        statementInfo.setWidthPercentage(100);
        statementInfo.setSpacingBefore(10f);

        PdfPCell statementStartTime = new PdfPCell(new Phrase("Statement Start Date: " + statementRequest.getStartDate(), FontFactory.getFont(FontFactory.HELVETICA, 12)));
        statementStartTime.setBorder(Rectangle.NO_BORDER);

        PdfPCell statement = new PdfPCell(new Phrase("STATEMENT OF ACCOUNT", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
        statement.setHorizontalAlignment(Element.ALIGN_CENTER);
        statement.setBorder(Rectangle.NO_BORDER);

        PdfPCell statementEndTime = new PdfPCell(new Phrase("Statement End Date: " + statementRequest.getEndDate(), FontFactory.getFont(FontFactory.HELVETICA, 12)));
        statementEndTime.setBorder(Rectangle.NO_BORDER);

        PdfPCell statementCustomerName = new PdfPCell(new Phrase("Customer Name: " + fullName, FontFactory.getFont(FontFactory.HELVETICA, 12)));
        statementCustomerName.setBorder(Rectangle.NO_BORDER);

        PdfPCell space = new PdfPCell(new Phrase(""));
        space.setBorder(Rectangle.NO_BORDER);

        PdfPCell statementCustomerAddress = new PdfPCell(new Phrase("Address: " + fullAddress, FontFactory.getFont(FontFactory.HELVETICA, 12)));
        statementCustomerAddress.setBorder(Rectangle.NO_BORDER);

        statementInfo.addCell(statementStartTime);
        statementInfo.addCell(statement);
        statementInfo.addCell(statementEndTime);
        statementInfo.addCell(statementCustomerName);
        statementInfo.addCell(space);
        statementInfo.addCell(statementCustomerAddress);

        // Transaction Information Table
        PdfPTable transactionInfo = new PdfPTable(4);
        transactionInfo.setWidthPercentage(100);
        transactionInfo.setSpacingBefore(10f);

        PdfPCell transactionDate = new PdfPCell(new Phrase("Date", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
        transactionDate.setBackgroundColor(BaseColor.BLUE);
        transactionDate.setHorizontalAlignment(Element.ALIGN_CENTER);
        transactionDate.setPadding(8f);
        transactionDate.setBorder(Rectangle.BOX);

        PdfPCell transactionType = new PdfPCell(new Phrase("Transaction Type", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
        transactionType.setBackgroundColor(BaseColor.BLUE);
        transactionType.setHorizontalAlignment(Element.ALIGN_CENTER);
        transactionType.setPadding(8f);
        transactionType.setBorder(Rectangle.BOX);

        PdfPCell transactionAmount = new PdfPCell(new Phrase("Transaction Amount", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
        transactionAmount.setBackgroundColor(BaseColor.BLUE);
        transactionAmount.setHorizontalAlignment(Element.ALIGN_CENTER);
        transactionAmount.setPadding(8f);
        transactionAmount.setBorder(Rectangle.BOX);

        PdfPCell transactionStatus = new PdfPCell(new Phrase("Transaction Status", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
        transactionStatus.setBackgroundColor(BaseColor.BLUE);
        transactionStatus.setHorizontalAlignment(Element.ALIGN_CENTER);
        transactionStatus.setPadding(8f);
        transactionStatus.setBorder(Rectangle.BOX);

        transactionInfo.addCell(transactionDate);
        transactionInfo.addCell(transactionType);
        transactionInfo.addCell(transactionAmount);
        transactionInfo.addCell(transactionStatus);

        transactions.forEach(transaction -> {
            PdfPCell dateCell = new PdfPCell(new Phrase(transaction.getCreatedAt().toString(), FontFactory.getFont(FontFactory.HELVETICA, 12)));
            dateCell.setPadding(5f);
            dateCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            dateCell.setBorder(Rectangle.BOX);

            PdfPCell typeCell = new PdfPCell(new Phrase(transaction.getTransactionType(), FontFactory.getFont(FontFactory.HELVETICA, 12)));
            typeCell.setPadding(5f);
            typeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            typeCell.setBorder(Rectangle.BOX);

            PdfPCell amountCell = new PdfPCell(new Phrase(transaction.getAmount().toString(), FontFactory.getFont(FontFactory.HELVETICA, 12)));
            amountCell.setPadding(5f);
            amountCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            amountCell.setBorder(Rectangle.BOX);

            PdfPCell statusCell = new PdfPCell(new Phrase(transaction.getStatus(), FontFactory.getFont(FontFactory.HELVETICA, 12)));
            statusCell.setPadding(5f);
            statusCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            statusCell.setBorder(Rectangle.BOX);

            transactionInfo.addCell(dateCell);
            transactionInfo.addCell(typeCell);
            transactionInfo.addCell(amountCell);
            transactionInfo.addCell(statusCell);
        });

        //Balance info

        PdfPTable balanceInfo = new PdfPTable(1);
        PdfPCell currentBalance = new PdfPCell(new Phrase("Your Current Balance : " + statementUser.getAccountBalance(),FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
        currentBalance.setHorizontalAlignment(Element.ALIGN_CENTER);
        currentBalance.setBorder(Rectangle.NO_BORDER);
        balanceInfo.addCell(space);
        balanceInfo.addCell(currentBalance);


        // Adding elements to the document
        statementDocument.add(bankInfo);
        statementDocument.add(statementInfo);
        statementDocument.add(transactionInfo);
        statementDocument.add(balanceInfo);

        statementDocument.close();
    }

}
