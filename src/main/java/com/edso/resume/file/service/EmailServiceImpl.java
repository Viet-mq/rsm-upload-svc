package com.edso.resume.file.service;

import com.edso.resume.lib.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Objects;

@Service
public class EmailServiceImpl extends BaseService implements EmailService{

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${email.from.address}")
    private String fromAddress;

    public BaseResponse sendMailMultipart(String toEmail, String subject, String message, MultipartFile file) throws MessagingException {
        BaseResponse baseResponse = new BaseResponse();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(fromAddress);
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(message);

        if(file != null){
            helper.addAttachment(Objects.requireNonNull(file.getOriginalFilename()), file);
        }
        javaMailSender.send(mimeMessage);
        baseResponse.setSuccess("OK");

        return baseResponse;
    }

    public BaseResponse sendMail(String toEmail, String subject, String message) throws MessagingException {
        return sendMailMultipart(toEmail, subject, message, (MultipartFile) null);
    }

    public BaseResponse sendMail(String toEmail, String subject, String message, MultipartFile file) throws MessagingException {
        return sendMailMultipart(toEmail, subject, message, file);
    }
}
