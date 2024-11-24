package com.luv2code.springboot.banking.servicr;

import com.luv2code.springboot.banking.dto.EmailDetail;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EmailServiceImp implements EmailService {

    @Value("${spring.mail.username}")
    private String senderEmail;

    private final JavaMailSender javaMailSender;

    @Override
    public void sendEmailAlert(EmailDetail emailDetail) {
        try{
            SimpleMailMessage mailMessage =new SimpleMailMessage();
            mailMessage.setFrom(senderEmail);
            mailMessage.setTo(emailDetail.getRecipient());
            mailMessage.setSubject(emailDetail.getMessageSubject());
            mailMessage.setText(emailDetail.getMessageBody());

            javaMailSender.send(mailMessage);
            System.out.println("Mail sent successfully\n");
        }catch(MailException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendEmailWithAttachment(EmailDetail emailDetail) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper ;
        try{
            mimeMessageHelper=new MimeMessageHelper(mimeMessage,true);
            mimeMessageHelper.setFrom(senderEmail);
            mimeMessageHelper.setTo(emailDetail.getRecipient());
            mimeMessageHelper.setSubject(emailDetail.getMessageSubject());
            mimeMessageHelper.setText(emailDetail.getMessageBody());

            FileSystemResource file = new FileSystemResource(new File(emailDetail.getMessageAttachment()));
            mimeMessageHelper.addAttachment(Objects.requireNonNull(file.getFilename()), file);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }
}

